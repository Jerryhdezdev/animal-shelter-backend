package com.jerryhdez.animalshelter.exception;

// Custom exception thrown when an animal is not found in the database
public class AnimalNotFoundException extends RuntimeException {

    public AnimalNotFoundException(Long id){
        super("Animal not found with id " + id);
    }
}
