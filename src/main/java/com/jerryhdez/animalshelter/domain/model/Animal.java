package com.jerryhdez.animalshelter.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic information
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSpecies species;  // Fixed: was "Species" with uppercase S

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSex sex;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSize size;

    // Health information
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalVaccinationStatus vaccinationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSterilizationStatus sterilizationStatus;

    // Shelter information
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptionStatus status;

    @Column(nullable = false)
    private LocalDate intakeDate;

    @Lob
    @Column(nullable = false)
    private String description;

    // Audit information
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    // Automatically sets createdAt when the entity is first saved
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

}