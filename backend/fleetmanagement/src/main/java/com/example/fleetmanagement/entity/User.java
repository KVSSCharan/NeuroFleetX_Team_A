package com.example.fleetmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    // Fleet Manager specific
    @Column(name = "company_name")
    private String companyName;

    // Driver specific
    @Column(name = "license_no")
    private String licenseNo;

    // Customer specific
    @Column(name = "govt_id")
    private String govtId;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Getters & Setters

    public Long getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public String getGovtId() { return govtId; }
    public void setGovtId(String govtId) { this.govtId = govtId; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}