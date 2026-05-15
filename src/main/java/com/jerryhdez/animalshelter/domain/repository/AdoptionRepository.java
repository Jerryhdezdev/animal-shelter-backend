package com.jerryhdez.animalshelter.domain.repository;

import com.jerryhdez.animalshelter.domain.model.Adoption;
import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    // Find all adoptions by adopter
    List<Adoption> findByAdopterId(Long adopterId);

    // Find all adoptions by animal
    List<Adoption> findByAnimalId(Long animalId);

    // Find all adoptions by status
    List<Adoption> findByStatus(AdoptionStatus status);

    // Find all adoptions processed by a specific employee/admin
    List<Adoption> findByProcessedById(Long processedById);

    // Check if an animal already has an active adoption process
    boolean existsByAnimalIdAndStatusIn(Long animalId, List<AdoptionStatus> statuses);
}