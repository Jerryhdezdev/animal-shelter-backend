package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Adoption;
import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.repository.AdoptionRepository;
import com.jerryhdez.animalshelter.domain.repository.AnimalRepository;
import com.jerryhdez.animalshelter.exception.AdoptionNotFoundException;
import com.jerryhdez.animalshelter.web.dto.AdoptionResponseDTO;
import com.jerryhdez.animalshelter.web.dto.AdoptionRequestDTO;
import com.jerryhdez.animalshelter.web.mapper.AdoptionMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdoptionServiceTest {

    @Mock
    private AdoptionRepository adoptionRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AdoptionMapper adoptionMapper;

    @InjectMocks
    private AdoptionService adoptionService;

    // Helper method - builds a test adoption to reuse across tests
    private Animal createTestAnimal() {
        Animal animal = new Animal();
        animal.setName("Fiona");
        return animal;
    }

    private User createTestUser() {
        User user = new User();
        user.setFirstName("Jerry");
        user.setLastName("Hdez");
        return user;
    }

    private AdoptionResponseDTO createTestResponseDTO() {
        AdoptionResponseDTO responseDTO = new AdoptionResponseDTO();
        responseDTO.setAnimalName("Fiona");
        responseDTO.setUserName("Jerry Hdez");
        responseDTO.setAdoptionStatus("REQUESTED");
        return responseDTO;
    }

    private Adoption createTestAdoption() {
        Adoption adoption = new Adoption();
        adoption.setId(1L);
        adoption.setAnimal(createTestAnimal());
        adoption.setAdopter(createTestUser());
        adoption.setStatus(AdoptionStatus.REQUESTED);
        return adoption;
    }

    @Test
    void shouldReturnAllAdoptions() {
        // ARRANGE
        Adoption adoption = createTestAdoption();
        AdoptionResponseDTO responseDTO = createTestResponseDTO();

        when(adoptionRepository.findAll()).thenReturn(List.of(adoption));
        when(adoptionMapper.toResponse(adoption)).thenReturn(responseDTO);

        // ACT
        List<AdoptionResponseDTO> result = adoptionService.getAllAdoptions();

        // ASSERT
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAnimalName()).isEqualTo("Fiona");
        assertThat(result.get(0).getUserName()).isEqualTo("Jerry Hdez");
        verify(adoptionRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAdoptionWhenIdExists() {
        // ARRANGE
        Adoption adoption = createTestAdoption();
        AdoptionResponseDTO responseDTO = createTestResponseDTO();

        when(adoptionRepository.findById(1L)).thenReturn(Optional.of(adoption));
        when(adoptionMapper.toResponse(adoption)).thenReturn(responseDTO);

        // ACT
        AdoptionResponseDTO result = adoptionService.getAdoptionById(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getAnimalName()).isEqualTo("Fiona");
        assertThat(result.getUserName()).isEqualTo("Jerry Hdez");
        verify(adoptionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAdoptionNotFound() {
        // ARRANGE
        when(adoptionRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> adoptionService.getAdoptionById(13L))
                .isInstanceOf(AdoptionNotFoundException.class)
                .hasMessageContaining("Adoption not found with id");
        verify(adoptionRepository, times(1)).findById(13L);
    }

    @Test
    void shouldCreateAdoption() {
        // ARRANGE
        AdoptionRequestDTO requestDTO = new AdoptionRequestDTO();
        requestDTO.setAnimalId(1L);

        User testUser = createTestUser();
        Animal testAnimal = createTestAnimal();
        Adoption testAdoption = createTestAdoption();
        AdoptionResponseDTO responseDTO = createTestResponseDTO();

        when(animalRepository.findById(1L)).thenReturn(Optional.of(testAnimal));
        when(adoptionMapper.toEntity(requestDTO, testUser, testAnimal)).thenReturn(testAdoption);
        when(adoptionRepository.save(testAdoption)).thenReturn(testAdoption);
        when(adoptionMapper.toResponse(testAdoption)).thenReturn(responseDTO);

        // ACT
        AdoptionResponseDTO result = adoptionService.createAdoption(requestDTO, testUser);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getAnimalName()).isEqualTo("Fiona");
        assertThat(result.getUserName()).isEqualTo("Jerry Hdez");
        assertThat(result.getAdoptionStatus()).isEqualTo("REQUESTED");
        verify(adoptionRepository, times(1)).save(testAdoption);
    }

    @Test
    void shouldUpdateAdoption() {
        // ARRANGE
        Adoption existingAdoption = createTestAdoption();
        Adoption updatedAdoption = createTestAdoption();
        AdoptionResponseDTO responseDTO = createTestResponseDTO();
        responseDTO.setAdoptionStatus("APPROVED");

        when(adoptionRepository.findById(1L)).thenReturn(Optional.of(existingAdoption));
        when(adoptionRepository.save(any(Adoption.class))).thenReturn(updatedAdoption);
        when(adoptionMapper.toResponse(updatedAdoption)).thenReturn(responseDTO);

        // ACT
        AdoptionResponseDTO result = adoptionService.updateAdoption(1L, AdoptionStatus.APPROVED);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getAdoptionStatus()).isEqualTo("APPROVED");
        verify(adoptionRepository, times(1)).findById(1L);
        verify(adoptionRepository, times(1)).save(any(Adoption.class));

    }

    @Test
    void shouldThrowExceptionWhenUpdatingAdoptionNonexistent(){
        //ARRANGE
        when(adoptionRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> adoptionService.updateAdoption(13L, AdoptionStatus.APPROVED))
                .isInstanceOf(AdoptionNotFoundException.class)
                .hasMessageContaining("Adoption not found with id");
        verify(adoptionRepository, times(1)).findById(13L);

    }

    @Test
    void shouldDeleteAdoption() {
        // ARRANGE
        Adoption existingAdoption = createTestAdoption();
        when(adoptionRepository.findById(1L)).thenReturn(Optional.of(existingAdoption));

        // ACT
        adoptionService.deleteAdoption(1L);

        // ASSERT
        verify(adoptionRepository, times(1)).findById(1L);
        verify(adoptionRepository, times(1)).delete(existingAdoption);

    }

    @Test
    void shouldThrowExceptionWhenDeletingAdoptionNonexistent(){
        // ARRANGE
        when(adoptionRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> adoptionService.deleteAdoption(13L))
                .isInstanceOf(AdoptionNotFoundException.class)
                .hasMessageContaining("Adoption not found with id");
        verify(adoptionRepository, times(1)).findById(13L);


    }

}