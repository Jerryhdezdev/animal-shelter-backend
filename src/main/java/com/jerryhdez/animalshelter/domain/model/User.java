package com.jerryhdez.animalshelter.domain.model;

import com.jerryhdez.animalshelter.domain.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic information
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    // Authentification credentials
    @Column(nullable = false)
    private String passwordHash;

    // Role and permissions
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles role;

    // User status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    // Audit information
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime emailVerifiedAt;

    // Automatically sets timestamps on create and update
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
