package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.model.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.model.AnimalSpecies;
import com.jerryhdez.animalshelter.domain.model.AnimalSex;
import com.jerryhdez.animalshelter.domain.model.AnimalSize;
import com.jerryhdez.animalshelter.domain.model.AnimalVaccinationStatus;
import com.jerryhdez.animalshelter.domain.model.AnimalSterilizationStatus;
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

    @InjectMocks
    private AnimalService animalService;

    // Helper method - builds a test animal to reuse across tests
    private Animal buildTestAnimal() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Max");
        animal.setSpecies(AnimalSpecies.DOG);
        animal.setSex(AnimalSex.MALE);
        animal.setBirthDate(LocalDate.of(2026,1,2));
        animal.setWeight(new BigDecimal("25.25"));
        animal.setSize(AnimalSize.LARGE);
        animal.setVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        animal.setSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        animal.setStatus(AdoptionStatus.INTAKE_ASSESSMENT);
        animal.setIntakeDate(LocalDate.now());
        animal.setDescription("Max is a friendly dog");
        return animal;
    }

    @Test
    void shouldReturnAllAnimals(){
        // ARRANGE - prepare test data
        Animal animal = buildTestAnimal();
        when(animalRepository.findAll()).thenReturn(List.of(animal));

        // ACT - execute the method
        List<Animal> result = animalService.getAllAnimals();

        // ASSERT - verify the result
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Max");
        verify(animalRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAnimalWhenIdExists(){
        // ARRANGE
        Animal animal = buildTestAnimal();
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        // ACT
        Animal result = animalService.getAnimalById(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
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
                .hasMessageContaining("Animal not found");
        verify(animalRepository, times(1)).findById(13L);
    }

    @Test
    void shouldSaveAnimalSuccessfully(){
        // ARRANGE
        Animal animal = buildTestAnimal();
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        // ACT
        Animal result = animalService.saveAnimal(animal);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldUpdateAnimalSuccessfully(){
        // ARRANGE
        Animal existingAnimal = buildTestAnimal();
        Animal updatedAnimal = buildTestAnimal();
        updatedAnimal.setName("Max Updated");
        updatedAnimal.setWeight(new BigDecimal("35.0"));

        when(animalRepository.findById(1L)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(any(Animal.class))).thenReturn(updatedAnimal);

        // ACT
        Animal result = animalService.updateAnimal(1L, updatedAnimal);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max Updated");
        assertThat(result.getWeight()).isEqualTo(new BigDecimal("35.0"));
        verify(animalRepository, times(1)).findById(1L);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentAnimal(){
        // ARRANGE
        Animal updatedAnimal = buildTestAnimal();
        when(animalRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> animalService.updateAnimal(13L, updatedAnimal))
                .isInstanceOf(AnimalNotFoundException.class)
                .hasMessageContaining("Animal not found");
        verify(animalRepository, times(1)).findById(13L);
    }

    @Test
    void shouldDeleteAnimalSuccessfully(){
        // ARRANGE
        Animal animal = buildTestAnimal();
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
                .hasMessageContaining("Animal not found");
        verify(animalRepository, times(1)).findById(13L);
    }

}
