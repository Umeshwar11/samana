package com.samana.entity;

import jakarta.persistence.*;
import com.samana.enums.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // BASIC USER INFORMATION
    // =====================================================

    private String name;

    @Column(unique = true)
    private String email;

    private String password;


    // =====================================================
    // USER ROLE
    // =====================================================

    @Enumerated(EnumType.STRING)
    private Role role;


    // =====================================================
    // HOST PROFILE
    // =====================================================

    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private HostProfile hostProfile;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public User() {
    }


    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    // =====================================================
    // HOST PROFILE GETTER / SETTER
    // =====================================================

    public HostProfile getHostProfile() {
        return hostProfile;
    }

    public void setHostProfile(HostProfile hostProfile) {
        this.hostProfile = hostProfile;
    }
}