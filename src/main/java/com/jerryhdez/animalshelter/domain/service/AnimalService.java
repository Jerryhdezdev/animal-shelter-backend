package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.repository.AnimalRepository;
import com.jerryhdez.animalshelter.exception.AnimalNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    // Gets all animals from the database
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    // Retrieves a single animal by id - Throws exception if not found
    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));
    }

    // Saves a new animal to the database
    public Animal saveAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    // Updates an existing animal - throws exception if not found
    public Animal updateAnimal(Long id, Animal updateAnimal){

        // First verifies the animal exists - throws exceptions if not
        Animal existingAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));

        // Updates only the fields that are allowed to change
        existingAnimal.setName(updateAnimal.getName());
        existingAnimal.setSpecies(updateAnimal.getSpecies());
        existingAnimal.setSex(updateAnimal.getSex());
        existingAnimal.setBirthDate(updateAnimal.getBirthDate());
        existingAnimal.setWeight(updateAnimal.getWeight());
        existingAnimal.setSize(updateAnimal.getSize());
        existingAnimal.setVaccinationStatus(updateAnimal.getVaccinationStatus());
        existingAnimal.setSterilizationStatus(updateAnimal.getSterilizationStatus());
        existingAnimal.setDescription(updateAnimal.getDescription());

        // Saves and returns the updated animal
        return animalRepository.save(existingAnimal);
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
