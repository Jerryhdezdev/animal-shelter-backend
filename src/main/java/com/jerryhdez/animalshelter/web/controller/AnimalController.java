package com.jerryhdez.animalshelter.web.controller;

// Import REST annotations
import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.service.AnimalService;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;
import com.jerryhdez.animalshelter.web.mapper.AnimalMapper;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

// Import List
import java.util.List;


// Marks this class as REST controller
@RestController

// Base URL for this controller
@RequestMapping("/animals")
public class AnimalController {

    //Service reference
    private final AnimalService animalService;
    private final AnimalMapper animalMapper;

    //Constructor injection
    public AnimalController(AnimalService animalService,
                            AnimalMapper animalMapper) {
        this.animalService = animalService;
        this.animalMapper = animalMapper;
    }


    // GET all animals
    @GetMapping
    public List<AnimalResponseDTO> getAllAnimals() {
        return animalService.getAllAnimals()
                .stream()
                .map(animalMapper::toResponse)
                .toList();
    }

    // POST create animal
    @PostMapping
    public AnimalResponseDTO saveAnimal(
            @Valid @RequestBody AnimalRequestDTO request) {

        // Convert DTO to Entity
        Animal animal = animalMapper.toEntity(request);

        // Save entity
        Animal savedAnimal = animalService.saveAnimal(animal);

        // Convert back to ResponseDTO
        return animalMapper.toResponse(savedAnimal);
    }
}
