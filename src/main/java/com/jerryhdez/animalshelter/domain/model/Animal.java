package com.jerryhdez.animalshelter.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSpecies Species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSex sex;

    @Column (nullable = false)
    private LocalDate birthDate;

    @Column (nullable = false)
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSize size;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private AnimalVaccinationStatus vaccinationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSterilizationStatus sterilizationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptionStatus status;

    @Column(nullable = false)
    private LocalDate intakeDate;

    @Column(nullable = false, updatable=false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @Lob
    @Column(nullable = false)
    private String description;
}
