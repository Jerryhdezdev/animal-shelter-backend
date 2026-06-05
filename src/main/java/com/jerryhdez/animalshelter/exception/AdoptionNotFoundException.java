package com.jerryhdez.animalshelter.exception;

// Custom exception thrown when an adoption is not found in the database
public class AdoptionNotFoundException extends RuntimeException {
    public AdoptionNotFoundException(Long id)
    {
        super("Adoption not found with id " + id);
    }
}
