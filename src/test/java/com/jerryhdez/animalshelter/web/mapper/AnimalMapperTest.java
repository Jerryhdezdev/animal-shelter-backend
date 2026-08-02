package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.enums.AnimalVaccinationStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalSterilizationStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalAdoptionStatus;
import com.jerryhdez.animalshelter.domain.enums.AnimalSex;
import com.jerryhdez.animalshelter.domain.enums.AnimalSize;
import com.jerryhdez.animalshelter.domain.enums.AnimalSpecies;
import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.web.dto.AnimalRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AnimalResponseDTO;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AnimalMapperTest {

    // No mocks needed — AnimalMapper has no dependencies
    private final AnimalMapper animalMapper = new AnimalMapper();

    // Helper method — builds a test request DTO
    private AnimalRequestDTO createTestRequestDTO() {
        AnimalRequestDTO requestDTO = new AnimalRequestDTO();
        requestDTO.setName("Max");
        requestDTO.setAnimalSpecies(AnimalSpecies.DOG);
        requestDTO.setAnimalSex(AnimalSex.MALE);
        requestDTO.setBirthDate(LocalDate.of(2021, 5, 10));
        requestDTO.setWeight(new BigDecimal("25.5"));
        requestDTO.setAnimalSize(AnimalSize.LARGE);
        requestDTO.setAnimalVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        requestDTO.setAnimalSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        requestDTO.setDescription("Max is a friendly dog");
        return requestDTO;
    }

    // Helper method — builds a test animal entity
    private Animal createTestAnimal() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Max");
        animal.setSpecies(AnimalSpecies.DOG);
        animal.setSex(AnimalSex.MALE);
        animal.setBirthDate(LocalDate.of(2021, 5, 10));
        animal.setWeight(new BigDecimal("25.5"));
        animal.setSize(AnimalSize.LARGE);
        animal.setVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        animal.setSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        animal.setStatus(AnimalAdoptionStatus.INTAKE_ASSESSMENT);
        animal.setIntakeDate(LocalDate.now());
        animal.setDescription("Max is a friendly dog");
        return animal;
    }

    @Test
    void shouldMapRequestDTOToEntity() {
        // ARRANGE
        AnimalRequestDTO requestDTO = createTestRequestDTO();

        // ACT
        Animal animal = animalMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(animal.getName()).isEqualTo("Max");
        assertThat(animal.getSpecies()).isEqualTo(AnimalSpecies.DOG);
        assertThat(animal.getSex()).isEqualTo(AnimalSex.MALE);
        assertThat(animal.getBirthDate()).isEqualTo(LocalDate.of(2021, 5, 10));
        assertThat(animal.getWeight()).isEqualTo(new BigDecimal("25.5"));
        assertThat(animal.getSize()).isEqualTo(AnimalSize.LARGE);
        assertThat(animal.getVaccinationStatus()).isEqualTo(AnimalVaccinationStatus.FULL_VACCINATED);
        assertThat(animal.getSterilizationStatus()).isEqualTo(AnimalSterilizationStatus.STERILIZED);
        assertThat(animal.getDescription()).isEqualTo("Max is a friendly dog");
    }

    @Test
    void shouldSetIntakeDateAutomaticallyWhenMappingToEntity() {
        // ARRANGE
        AnimalRequestDTO requestDTO = createTestRequestDTO();

        // ACT
        Animal animal = animalMapper.toEntity(requestDTO);

        // ASSERT — intakeDate should be set automatically to today
        assertThat(animal.getIntakeDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldSetStatusToIntakeAssessmentWhenMappingToEntity() {
        // ARRANGE
        AnimalRequestDTO requestDTO = createTestRequestDTO();

        // ACT
        Animal animal = animalMapper.toEntity(requestDTO);

        // ASSERT — every new animal starts with INTAKE_ASSESSMENT
        assertThat(animal.getStatus()).isEqualTo(AnimalAdoptionStatus.INTAKE_ASSESSMENT);
    }

    @Test
    void shouldMapEntityToResponseDTO() {
        // ARRANGE
        Animal animal = createTestAnimal();

        // ACT
        AnimalResponseDTO responseDTO = animalMapper.toResponse(animal);

        // ASSERT
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getName()).isEqualTo("Max");
        assertThat(responseDTO.getSpecies()).isEqualTo("DOG");
        assertThat(responseDTO.getSex()).isEqualTo("MALE");
        assertThat(responseDTO.getBirthDate()).isEqualTo(LocalDate.of(2021, 5, 10));
        assertThat(responseDTO.getWeight()).isEqualTo(new BigDecimal("25.5"));
        assertThat(responseDTO.getSize()).isEqualTo("LARGE");
        assertThat(responseDTO.getVaccinationStatus()).isEqualTo("FULL_VACCINATED");
        assertThat(responseDTO.getSterilizationStatus()).isEqualTo("STERILIZED");
        assertThat(responseDTO.getAdoptionStatus()).isEqualTo("INTAKE_ASSESSMENT");
        assertThat(responseDTO.getDescription()).isEqualTo("Max is a friendly dog");
    }

    @Test
    void shouldConvertEnumsToStringsWhenMappingToResponseDTO() {
        // ARRANGE
        Animal animal = createTestAnimal();

        // ACT
        AnimalResponseDTO responseDTO = animalMapper.toResponse(animal);

        // ASSERT — enums must be converted to String in the response
        assertThat(responseDTO.getSpecies()).isInstanceOf(String.class);
        assertThat(responseDTO.getSex()).isInstanceOf(String.class);
        assertThat(responseDTO.getSize()).isInstanceOf(String.class);
        assertThat(responseDTO.getVaccinationStatus()).isInstanceOf(String.class);
        assertThat(responseDTO.getSterilizationStatus()).isInstanceOf(String.class);
        assertThat(responseDTO.getAdoptionStatus()).isInstanceOf(String.class);
    }
}
