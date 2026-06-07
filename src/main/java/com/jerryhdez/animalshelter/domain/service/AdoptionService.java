package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Adoption;
import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.repository.AdoptionRepository;
import com.jerryhdez.animalshelter.exception.AdoptionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;

    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }

    // Gets all adoptions request from the database
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    // Retrieves a single adoption request by id - throws exception if not found
    public Adoption findById(long id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));
    }

    // Saves a new adoption request to the database
    public Adoption saveAdoption(Adoption adoption) {

        // System automatically assigns default values
        adoption.setStatus(AdoptionStatus.REQUESTED);

        return adoptionRepository.save(adoption);
    }

    // For SHELTER STAFF only - updates the adoption status through the process
    public Adoption updateStatus(long id, AdoptionStatus newStatus) {

        // First verifies if the adoption request exists - throws exception if not
        Adoption existingAdoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));

        // Shelter staff can move status
        existingAdoption.setStatus(newStatus);

        return adoptionRepository.save(existingAdoption);
    }

    // For USER only - can only cancel their own adoption request
    public Adoption cancelAdoption(long id) {

        // First verifies if the adoption request exists - throws exception if not
        Adoption existingAdoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));

        // User can only cancel - no other status change is allowed
        existingAdoption.setStatus(AdoptionStatus.CANCELLED);

        return adoptionRepository.save(existingAdoption);
    }

    // Deletes an existing adoption request - throws exceptions if not found
    public void deleteAdoption(long id) {

        // First verifies if the adoption request exists - throws exception if not
        Adoption existingAdoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));
        adoptionRepository.delete(existingAdoption);
    }

}
