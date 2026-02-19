package com.jerryhdez.animalshelter.web.controller;

// Import REST annotations
import org.springframework.web.bind.annotation.*;

// Import List
import java.util.List;

//Import Animal Class
import com.jerryhdez.animalshelter.domain.model.Animal;

//import Service
import com.jerryhdez.animalshelter.domain.service.AnimalService;

// Marks this class as REST controller
@RestController

// Base URL for this controller
@RequestMapping("/animals")
public class AnimalController {

    //Service reference
    private final AnimalService animalService;

    //Constructor injection
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // GET all animals
    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    // POST create animal
    @PostMapping
    public Animal saveAnimal(@RequestBody Animal animal) {
        return animalService.saveAnimal(animal);
    }
}
