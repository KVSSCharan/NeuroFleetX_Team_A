package com.example.fleetmanagement.service;

import com.example.fleetmanagement.dto.RouteRequest;
import com.example.fleetmanagement.dto.RouteResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteOptimizationService {

    private final ETAService etaService;

    public RouteOptimizationService(ETAService etaService) {
        this.etaService = etaService;
    }

    public List<RouteResponse> getOptimizedRoutes(RouteRequest request) {

        double startLat = request.getStartLat();
        double startLng = request.getStartLng();
        double endLat = request.getEndLat();
        double endLng = request.getEndLng();

        try {
            List<Node> nodes = generateNodes(startLat, startLng, endLat, endLng);
            Map<Node, List<Edge>> graph = buildGraph(nodes);

            Node startNode = findClosest(nodes, startLat, startLng);
            Node endNode = findClosest(nodes, endLat, endLng);

            List<Node> path = dijkstra(graph, startNode, endNode);

            if (path == null || path.isEmpty()) {
                return Collections.emptyList();
            }

            List<List<Double>> coordinates = new ArrayList<>();
            double totalDistance = 0;

            for (int i = 0; i < path.size(); i++) {
                Node n = path.get(i);
                coordinates.add(Arrays.asList(n.lat, n.lng));

                if (i > 0) {
                    totalDistance += distance(path.get(i - 1), n);
                }
            }

            String eta = etaService.calculateETA(totalDistance);

            return List.of(new RouteResponse(totalDistance, eta, coordinates));

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    static class Node {
        double lat, lng;

        Node(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    static class Edge {
        Node target;
        double weight;

        Edge(Node target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    private List<Node> generateNodes(double lat1, double lng1, double lat2, double lng2) {
        List<Node> nodes = new ArrayList<>();

        int steps = 10;

        for (int i = 0; i <= steps; i++) {
            double lat = lat1 + (lat2 - lat1) * i / steps;
            double lng = lng1 + (lng2 - lng1) * i / steps;

            nodes.add(new Node(lat, lng));
            nodes.add(new Node(lat + 0.01, lng));
            nodes.add(new Node(lat, lng + 0.01));
        }

        return nodes;
    }

    private Map<Node, List<Edge>> buildGraph(List<Node> nodes) {
        Map<Node, List<Edge>> graph = new HashMap<>();

        for (Node n1 : nodes) {
            graph.put(n1, new ArrayList<>());

            for (Node n2 : nodes) {
                if (n1 != n2) {
                    double dist = distance(n1, n2);

                    if (dist < 5) {
                        graph.get(n1).add(new Edge(n2, dist));
                    }
                }
            }
        }

        return graph;
    }

    private List<Node> dijkstra(Map<Node, List<Edge>> graph, Node start, Node end) {

        Map<Node, Double> dist = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (Node n : graph.keySet()) {
            dist.put(n, Double.MAX_VALUE);
        }

        dist.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current == end) break;

            for (Edge edge : graph.get(current)) {
                double newDist = dist.get(current) + edge.weight;

                if (newDist < dist.get(edge.target)) {
                    dist.put(edge.target, newDist);
                    prev.put(edge.target, current);
                    pq.add(edge.target);
                }
            }
        }

        List<Node> path = new ArrayList<>();
        for (Node at = end; at != null; at = prev.get(at)) {
            path.add(at);
        }

        Collections.reverse(path);
        return path;
    }

    private double distance(Node a, Node b) {
        double R = 6371;

        double dLat = Math.toRadians(b.lat - a.lat);
        double dLon = Math.toRadians(b.lng - a.lng);

        double aa = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(a.lat)) * Math.cos(Math.toRadians(b.lat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1 - aa));

        return R * c;
    }

    private Node findClosest(List<Node> nodes, double lat, double lng) {
        return nodes.stream()
                .min(Comparator.comparingDouble(n ->
                        Math.pow(n.lat - lat, 2) + Math.pow(n.lng - lng, 2)))
                .orElse(nodes.get(0));
    }
}