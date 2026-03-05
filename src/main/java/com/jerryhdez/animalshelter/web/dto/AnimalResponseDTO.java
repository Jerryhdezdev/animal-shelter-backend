package com.jerryhdez.animalshelter.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AnimalResponseDTO {

    // Unique identifier assigned by the database
    private Long id;

    // Basic information
    private String name;
    private String species;
    private String sex;
    private LocalDate birthDate;
    private BigDecimal weight;
    private String size;

    // Health information
    private String vaccinationStatus;
    private String sterilizationStatus;

    // Shelter information
    private String adoptionStatus;
    private LocalDate intakeDate;
    private String description;
}