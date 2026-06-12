package com.jerryhdez.animalshelter.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdoptionResponseDTO {

    // Unique identifier of the adoption request itself
    private Long id;

    // Adoption request information
    private String animalName;
    private String userName;
    private String adoptionStatus;
    private LocalDate adoptionDate;

}
