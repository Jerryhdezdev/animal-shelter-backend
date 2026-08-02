package com.jerryhdez.animalshelter.web.controller;

import com.jerryhdez.animalshelter.domain.service.AnimalService;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {

    // Service dependencies
    private final AnimalService animalService;

    // Constructor injection
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // GET /api/v1/animals — retrieves all animals
    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> getAllAnimals() {
        List<AnimalResponseDTO> animals = animalService.getAllAnimals();
        return ResponseEntity.ok(animals);
    }

    // GET / api/v1/animals/{id} - retrieves a single animal by id
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable Long id){
        AnimalResponseDTO response = animalService.getAnimalById(id);
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/animals — creates a new animal
    @PostMapping
    public ResponseEntity<AnimalResponseDTO> createAnimal(
            @Valid @RequestBody AnimalRequestDTO request) {
        AnimalResponseDTO response = animalService.createAnimal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /api/v1/animals/{id} - updates an existing animal
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> updateAnimal(
            @PathVariable Long id,
            @Valid @RequestBody AnimalRequestDTO request){
        AnimalResponseDTO response = animalService.updateAnimal(id,request);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/animals/{id} - deletes an existing animal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id){
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }

}