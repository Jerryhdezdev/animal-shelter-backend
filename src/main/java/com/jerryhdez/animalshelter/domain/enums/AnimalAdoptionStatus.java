package com.jerryhdez.animalshelter.domain.enums;

public enum AnimalAdoptionStatus {
    INTAKE_ASSESSMENT,  // Animal just entered the shelter - under initial evaluation
    QUARANTINE,         // Animal isolated due to health concerns
    MEDICAL_HOLD,       // Animal receiving medical treatment
    AVAILABLE,          // Animal cleared and ready for adoption
    PENDING_ADOPTION,   // Adoption process started
    RESERVED,           // Reserved for a specific adopter
    ADOPTED             // Successfully adopted
}
