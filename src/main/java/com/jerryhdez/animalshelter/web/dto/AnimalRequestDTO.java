package com.jerryhdez.animalshelter.web.dto;

import com.jerryhdez.animalshelter.domain.enums.AnimalSpecies;
import com.jerryhdez.animalshelter.domain.enums.AnimalSex;
import com.jerryhdez.animalshelter.domain.enums.AnimalSize;
import com.jerryhdez.animalshelter.domain.enums.AnimalVaccinationStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalSterilizationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnimalRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Species is required")
    private AnimalSpecies animalSpecies;

    @NotNull(message = "Sex is required")
    private AnimalSex animalSex;

    @NotNull(message = "Birth date is required")
    @PastOrPresent(message = "Birth date cannot be a future date")
    private LocalDate birthDate;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be a positive number")
    private BigDecimal weight;

    @NotNull(message = "Size is required")
    private AnimalSize animalSize;

    @NotNull(message = "Vaccination status is required")
    private AnimalVaccinationStatus animalVaccinationStatus;

    @NotNull(message = "Sterilization status is required")
    private AnimalSterilizationStatus animalSterilizationStatus;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters")
    private String description;

}
