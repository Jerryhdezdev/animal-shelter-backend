package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Adoption;
import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.repository.AdoptionRepository;
import com.jerryhdez.animalshelter.domain.repository.AnimalRepository;
import com.jerryhdez.animalshelter.web.dto.AdoptionResponseDTO;
import com.jerryhdez.animalshelter.web.dto.AdoptionRequestDTO;
import com.jerryhdez.animalshelter.web.mapper.AdoptionMapper;
import com.jerryhdez.animalshelter.exception.AdoptionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AnimalRepository animalRepository;
    private final AdoptionMapper adoptionMapper;

    public AdoptionService(AdoptionRepository adoptionRepository,
                           AnimalRepository animalRepository,
                           AdoptionMapper adoptionMapper) {
        this.adoptionRepository = adoptionRepository;
        this.animalRepository = animalRepository;
        this.adoptionMapper = adoptionMapper;
    }

    // Gets all adoptions and converts them to response DTOs
    public List<AdoptionResponseDTO> getAllAdoptions() {
        return adoptionRepository.findAll()
                .stream()
                .map(adoptionMapper::toResponse)
                .toList();
    }

    // Gets a single adoption by id and converts it to response DTO
    public AdoptionResponseDTO getAdoptionById(long id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));
        return adoptionMapper.toResponse(adoption);
    }

    // Creates a new adoption request
    public AdoptionResponseDTO createAdoption(AdoptionRequestDTO request, User adopter) {
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Adoption not found"));
        Adoption adoption = adoptionMapper.toEntity(request, adopter, animal);
        Adoption saved = adoptionRepository.save(adoption);
        return adoptionMapper.toResponse(saved);
    }

    // For SHELTER STAFF only - updates the adoption status
    public AdoptionResponseDTO updateAdoption(long id, AdoptionStatus newStatus) {
        Adoption existing = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));
        existing.setStatus(newStatus);
        Adoption saved = adoptionRepository.save(existing);
        return adoptionMapper.toResponse(saved);
    }

    // For USER only - can only cancel their own adoption request
    public AdoptionResponseDTO cancelAdoption(long id) {
        Adoption existing = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));
        existing.setStatus(AdoptionStatus.CANCELLED);
        Adoption saved = adoptionRepository.save(existing);
        return adoptionMapper.toResponse(saved);
    }

    // Deletes an existing adoption request
    public void deleteAdoption(long id) {
        Adoption existing = adoptionRepository.findById(id)
                .orElseThrow(() -> new AdoptionNotFoundException(id));
        adoptionRepository.delete(existing);
    }
}
