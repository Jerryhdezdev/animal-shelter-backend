package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    public Animal toEntity(AnimalRequestDTO dto){
        Animal animal = new Animal();
        animal.setName(dto.getName());
        animal.setSpecies(dto.getAnimalSpecies());
        animal.setBirthDate(dto.getBirthDate());
        animal.setSize(dto.getAnimalSize());
        return animal;
    }
    public AnimalResponseDTO toResponse(Animal animal) {
        return new AnimalResponseDTO(
                animal.getId(),
                animal.getName(),
                animal.getSpecies().name(),
                animal.getBirthDate().toString(),
                animal.getSize().name()
        );
    }
}
