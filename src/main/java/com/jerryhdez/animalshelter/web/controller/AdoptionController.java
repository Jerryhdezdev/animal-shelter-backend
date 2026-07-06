package com.jerryhdez.animalshelter.web.controller;

import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.service.AdoptionService;
import com.jerryhdez.animalshelter.web.dto.AdoptionRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AdoptionResponseDTO;


import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adoptions")
public class AdoptionController {

    // Service dependency only
    private final AdoptionService adoptionService;

    // Constructor injection
    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    // GET /api/v1/adoptions/ - retrieves all adoptions
    @GetMapping
    public ResponseEntity<List<AdoptionResponseDTO>> getAllAdoptions() {
        List<AdoptionResponseDTO> adoptions = adoptionService.getAllAdoptions();
        return ResponseEntity.ok(adoptions); // HTTP 200
    }

    // GET /api/v1/adoptions/{id} - retrieves a single adoption by id
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionResponseDTO> getAdoptionById(@PathVariable long id) {
        AdoptionResponseDTO response = adoptionService.getAdoptionById(id);
        return ResponseEntity.ok(response); // HTTP 200
    }

    // POST /api/v1/adoptions — creates a new adoption
    @PostMapping
    public ResponseEntity<AdoptionResponseDTO> createAdoption(
            @Valid @RequestBody AdoptionRequestDTO request) {
        AdoptionResponseDTO response = adoptionService.createAdoption(request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201
    }

    // PATCH /api/v1/adoptions/{id}/status - updates adoption status (staff only)
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdoptionResponseDTO> updateAdoptionStatus(
            @PathVariable Long id,
            @RequestParam AdoptionStatus newStatus) {
        AdoptionResponseDTO response = adoptionService.updateAdoption(id, newStatus);
        return ResponseEntity.ok(response); // HTTP 200
    }

    // PATCH /api/v1/adoptions/{id}/cancel - cancels an adoption (user only)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AdoptionResponseDTO> cancelAdoption(@PathVariable Long id) {
        AdoptionResponseDTO response = adoptionService.cancelAdoption(id);
        return ResponseEntity.ok(response); // HTTP 200
    }

    // DELETE /api/v1/adoptions/{id} - deletes an existing adoption
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

}
