package com.jerryhdez.animalshelter.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptionRequestDTO {

    @NotNull(message = "Oops! Don't forget to choose your new friend!")
    private Long animalId;

}
