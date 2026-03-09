package com.example.fleetmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF
            .csrf(csrf -> csrf.disable())

            // Stateless JWT session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // Public login/signup
                .requestMatchers("/api/auth/**").permitAll()

                // Profile endpoint for any logged-in user
                .requestMatchers("/users/me").authenticated()

                // Fleet manager vehicle management
                .requestMatchers("/fleet/**").hasRole("FLEET_MANAGER")
                .requestMatchers("/vehicles/**").hasRole("FLEET_MANAGER")

                // Admin endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Driver telemetry endpoints
                .requestMatchers("/driver/**").hasRole("DRIVER")

                // Customer endpoints
                .requestMatchers("/customer/**").hasRole("CUSTOMER")

                // Manager dashboards
                .requestMatchers("/manager/**").hasRole("FLEET_MANAGER")

                // Any other request requires authentication
                .anyRequest().authenticated()
            )

            // Disable default login
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Use custom user details service
            .userDetailsService(customUserDetailsService);

        // Add JWT filter
        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}