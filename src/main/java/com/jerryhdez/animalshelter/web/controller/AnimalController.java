package com.jerryhdez.animalshelter.web.controller;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.service.AnimalService;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;
import com.jerryhdez.animalshelter.web.mapper.AnimalMapper;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Marks this class as a REST controller
@RestController

// Base URL for all endpoints in this controller
@RequestMapping("/api/v1/animals")
public class AnimalController {

    // Service and mapper dependencies
    private final AnimalService animalService;
    private final AnimalMapper animalMapper;

    // Constructor injection — recommended over @Autowired
    public AnimalController(AnimalService animalService,
                            AnimalMapper animalMapper) {
        this.animalService = animalService;
        this.animalMapper = animalMapper;
    }

    // GET /api/v1/animals — retrieves all animals
    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> getAllAnimals() {
        List<AnimalResponseDTO> animals = animalService.getAllAnimals()
                .stream()
                .map(animalMapper::toResponse)
                .toList();

        return ResponseEntity.ok(animals); // HTTP 200
    }

    // GET / api/v1/anmimal/{id} - retrieves a single animal by id
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable Long id){

        //Finds animal - throws AnimalNotFoundException if not found
        Animal animal = animalService.getAnimalById(id);

        //Converts entity to response DTO
        AnimalResponseDTO response = animalMapper.toResponse(animal);

        return ResponseEntity.ok(response); //HTTP 200
    }

    // POST /api/v1/animals — creates a new animal
    @PostMapping
    public ResponseEntity<AnimalResponseDTO> createAnimal(
            @Valid @RequestBody AnimalRequestDTO request) {

        // Converts request DTO to entity
        Animal animal = animalMapper.toEntity(request);

        // Saves entity to database
        Animal savedAnimal = animalService.saveAnimal(animal);

        // Converts saved entity back to response DTO
        AnimalResponseDTO response = animalMapper.toResponse(savedAnimal);

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201
    }

    // PUT /api/v1/animals/{id} - updates an existing animal
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> updateAnimal(
            @PathVariable Long id,
            @Valid @RequestBody AnimalRequestDTO request){

        // Converts request DTO to entity
        Animal updatedAnimal = animalMapper.toEntity(request);

        // Updates animal in database - throws AnimalNotFoundException if not found
        Animal savedAnimal = animalService.updateAnimal(id, updatedAnimal);

        // Converts updated entity to response DTO
        AnimalResponseDTO response = animalMapper.toResponse(savedAnimal);

        return ResponseEntity.ok(response); //HTTP 200
    }
    // DELETE /api/v1/animals/{id} - deletes an existing animal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id){

        // Deletes animal - throws AnimalNotFoundException if not found
        animalService.deleteAnimal(id);

        return ResponseEntity.noContent().build(); // HTTP 204
    }

}