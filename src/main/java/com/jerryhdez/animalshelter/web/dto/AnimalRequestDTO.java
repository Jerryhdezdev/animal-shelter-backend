package com.jerryhdez.animalshelter.web.dto;

import com.jerryhdez.animalshelter.domain.model.AnimalSpecies;
import com.jerryhdez.animalshelter.domain.model.AnimalSex;
import com.jerryhdez.animalshelter.domain.model.AnimalSize;
import com.jerryhdez.animalshelter.domain.model.AnimalVaccinationStatus;
import com.jerryhdez.animalshelter.domain.model.AnimalSterilizationStatus;

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

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private AnimalSpecies animalSpecies;

    @NotNull
    private AnimalSex animalSex;

    @NotNull
    @PastOrPresent
    private LocalDate birthDate;

    @NotNull
    @Positive
    private BigDecimal weight;

    @NotNull
    private AnimalSize animalSize;

    @NotNull
    private AnimalVaccinationStatus animalVaccinationStatus;

    @NotNull
    private AnimalSterilizationStatus animalSterilizationStatus;

    @NotBlank
    @Size(min = 2, max = 1000)
    private String description;

}
