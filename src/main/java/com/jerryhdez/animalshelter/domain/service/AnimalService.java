package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.web.mapper.AnimalMapper;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.domain.repository.AnimalRepository;
import com.jerryhdez.animalshelter.exception.AnimalNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    public AnimalService(AnimalRepository animalRepository,  AnimalMapper animalMapper) {

        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    // Gets all animals from the database
    public List<AnimalResponseDTO> getAllAnimals() {
        return animalRepository.findAll()
                .stream()
                .map(animalMapper::toResponse)
                .toList();
    }

    // Retrieves a single animal by id - Throws exception if not found
    public AnimalResponseDTO getAnimalById(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(()-> new AnimalNotFoundException(id));
        return animalMapper.toResponse(animal);
    }

    // Creates a new animal to the database
    public AnimalResponseDTO createAnimal(AnimalRequestDTO request) {
        Animal animal = animalMapper.toEntity(request);
        Animal savedAnimal = animalRepository.save(animal);

        return animalMapper.toResponse(savedAnimal);
    }

    // Updates an existing animal - throws exception if not found
    public AnimalResponseDTO updateAnimal(Long id, AnimalRequestDTO request) {

        // First verifies the animal exists - throws exceptions if not
        Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));

        // Updates only the fields that are allowed to change
        existingAnimal.setName(request.getName());
        existingAnimal.setSpecies(request.getAnimalSpecies());
        existingAnimal.setSex(request.getAnimalSex());
        existingAnimal.setBirthDate(request.getBirthDate());
        existingAnimal.setWeight(request.getWeight());
        existingAnimal.setSize(request.getAnimalSize());
        existingAnimal.setVaccinationStatus(request.getAnimalVaccinationStatus());
        existingAnimal.setSterilizationStatus(request.getAnimalSterilizationStatus());
        existingAnimal.setDescription(request.getDescription());

        // Saves and returns the updated animal
        Animal saved = animalRepository.save(existingAnimal);
        return animalMapper.toResponse(saved);
    }

    // Deletes an existing animal - throws exception if not found
    public void deleteAnimal(Long id){

        // First verify the animal exists - throws exception if not
        Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));

        //Deletes the animal from the database
        animalRepository.delete(existingAnimal);
    }
}
