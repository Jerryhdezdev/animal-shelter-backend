package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.enums.AnimalAdoptionStatus;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AnimalMapper {

    // Converts incoming request data (DTO) into an Animal entity ready to be saved
    public Animal toEntity(AnimalRequestDTO dto) {
        Animal animal = new Animal();

        // Basic information
        animal.setName(dto.getName());
        animal.setSpecies(dto.getAnimalSpecies());
        animal.setSex(dto.getAnimalSex());
        animal.setBirthDate(dto.getBirthDate());
        animal.setWeight(dto.getWeight());
        animal.setSize(dto.getAnimalSize());

        // Health information
        animal.setVaccinationStatus(dto.getAnimalVaccinationStatus());
        animal.setSterilizationStatus(dto.getAnimalSterilizationStatus());

        // Description
        animal.setDescription(dto.getDescription());

        // Fields assigned automatically by the system — not provided by the user
        animal.setIntakeDate(LocalDate.now());
        animal.setStatus(AnimalAdoptionStatus.INTAKE_ASSESSMENT);

        return animal;
    }

    // Converts an Animal entity into a response object to send back to the client
    public AnimalResponseDTO toResponse(Animal animal) {
        AnimalResponseDTO response = new AnimalResponseDTO();

        response.setId(animal.getId());
        response.setName(animal.getName());
        response.setSpecies(animal.getSpecies().name());
        response.setSex(animal.getSex().name());
        response.setBirthDate(animal.getBirthDate());
        response.setWeight(animal.getWeight());
        response.setSize(animal.getSize().name());
        response.setVaccinationStatus(animal.getVaccinationStatus().name());
        response.setSterilizationStatus(animal.getSterilizationStatus().name());
        response.setAdoptionStatus(animal.getStatus().name());
        response.setIntakeDate(animal.getIntakeDate());
        response.setDescription(animal.getDescription());

        return response;
    }
}
