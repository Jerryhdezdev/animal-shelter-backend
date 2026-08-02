package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;
import com.jerryhdez.animalshelter.web.mapper.AnimalMapper;
import com.jerryhdez.animalshelter.domain.enums.AnimalAdoptionStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalSpecies;
import com.jerryhdez.animalshelter.domain.enums.AnimalSex;
import com.jerryhdez.animalshelter.domain.enums.AnimalSize;
import com.jerryhdez.animalshelter.domain.enums.AnimalVaccinationStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalSterilizationStatus;
import com.jerryhdez.animalshelter.domain.repository.AnimalRepository;
import com.jerryhdez.animalshelter.exception.AnimalNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AnimalMapper animalMapper;

    @InjectMocks
    private AnimalService animalService;

    // Helper method - builds a test animal to reuse across tests
    private Animal createTestAnimal() {
        Animal animal = new Animal();
        animal.setName("Max");
        animal.setSpecies(AnimalSpecies.DOG);
        animal.setSex(AnimalSex.MALE);
        animal.setBirthDate(LocalDate.of(2026,1,2));
        animal.setWeight(new BigDecimal("25.25"));
        animal.setSize(AnimalSize.LARGE);
        animal.setVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        animal.setSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        animal.setStatus(AnimalAdoptionStatus.INTAKE_ASSESSMENT);
        animal.setIntakeDate(LocalDate.now());
        animal.setDescription("Max is a friendly dog");
        return animal;
    }

    private AnimalResponseDTO createTestAnimalResponseDTO() {
        AnimalResponseDTO responseDTO = new AnimalResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Max");
        responseDTO.setDescription("Max is a friendly dog");
        return responseDTO;
    }

    private AnimalRequestDTO createTestAnimalRequestDTO() {
        AnimalRequestDTO requestDTO = new AnimalRequestDTO();
        requestDTO.setName("Max Updated");
        return requestDTO;
    }

    @Test
    void shouldReturnAllAnimals(){
        // ARRANGE
        Animal animal = createTestAnimal();
        AnimalResponseDTO responseDTO = createTestAnimalResponseDTO();

        when(animalRepository.findAll()).thenReturn(List.of(animal));
        when(animalMapper.toResponse(animal)).thenReturn(responseDTO);

        // ACT
        List<AnimalResponseDTO> result = animalService.getAllAnimals();

        // ASSERT
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Max");
        verify(animalRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAnimalWhenIdExists(){
        // ARRANGE
        Animal animal = createTestAnimal();
        AnimalResponseDTO responseDTO = createTestAnimalResponseDTO();

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(animalMapper.toResponse(animal)).thenReturn(responseDTO);

        // ACT
        AnimalResponseDTO result = animalService.getAnimalById(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        verify(animalRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAnimalNotFound(){
        // ARRANGE
        when(animalRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> animalService.getAnimalById(13L))
                .isInstanceOf(AnimalNotFoundException.class)
                .hasMessageContaining("Animal with id 13 not found" );
        verify(animalRepository, times(1)).findById(13L);
    }

    @Test
    void shouldCreateAnimal(){
        // ARRANGE
        AnimalRequestDTO requestDTO = new AnimalRequestDTO();
        requestDTO.setName("Max");

        Animal animal = createTestAnimal();
        AnimalResponseDTO responseDTO = createTestAnimalResponseDTO();

        when(animalMapper.toEntity(requestDTO)).thenReturn(animal);
        when(animalRepository.save(animal)).thenReturn(animal);
        when(animalMapper.toResponse(animal)).thenReturn(responseDTO);

        // ACT
        AnimalResponseDTO result = animalService.createAnimal(requestDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        verify(animalRepository, times(1)).save(animal);
    }

    @Test
    void shouldUpdateAnimal(){
        // ARRANGE
        Animal existingAnimal = createTestAnimal();
        Animal updatedAnimal = createTestAnimal();
        AnimalResponseDTO responseDTO = createTestAnimalResponseDTO();
        AnimalRequestDTO requestDTO = createTestAnimalRequestDTO();
        responseDTO.setName("Max Updated");

        when(animalRepository.findById(1L)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(any(Animal.class))).thenReturn(updatedAnimal);
        when(animalMapper.toResponse(updatedAnimal)).thenReturn(responseDTO);

        // ACT
        AnimalResponseDTO result = animalService.updateAnimal(1L, requestDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max Updated");
        verify(animalRepository, times(1)).findById(1L);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentAnimal(){
        // ARRANGE
        AnimalRequestDTO requestDTO = createTestAnimalRequestDTO();
        when(animalRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> animalService.updateAnimal(13L, requestDTO))
                .isInstanceOf(AnimalNotFoundException.class)
                .hasMessageContaining("Animal with id 13 not found" );
        verify(animalRepository, times(1)).findById(13L);
    }

    @Test
    void shouldDeleteAnimal(){
        // ARRANGE
        Animal animal = createTestAnimal();
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        // ACT
        animalService.deleteAnimal(1L);

        // ASSERT
        verify(animalRepository, times(1)).findById(1L);
        verify(animalRepository, times(1)).delete(animal);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentAnimal(){
        // ARRANGE
        when(animalRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> animalService.deleteAnimal(13L))
                .isInstanceOf(AnimalNotFoundException.class)
                .hasMessageContaining("Animal with id 13 not found" );
        verify(animalRepository, times(1)).findById(13L);
    }

}
