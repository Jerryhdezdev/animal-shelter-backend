package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.model.*;
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
    private AnimalRequestDTO buildTestRequestDTO() {
        AnimalRequestDTO request = new AnimalRequestDTO();
        request.setName("Max");
        request.setAnimalSpecies(AnimalSpecies.DOG);
        request.setAnimalSex(AnimalSex.MALE);
        request.setBirthDate(LocalDate.of(2021, 5, 10));
        request.setWeight(new BigDecimal("25.5"));
        request.setAnimalSize(AnimalSize.LARGE);
        request.setAnimalVaccinationStatus(AnimalVaccinationStatus.FULL_VACCINATED);
        request.setAnimalSterilizationStatus(AnimalSterilizationStatus.STERILIZED);
        request.setDescription("Max is a friendly dog");
        return request;
    }

    // Helper method — builds a test animal entity
    private Animal buildTestAnimal() {
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
        animal.setStatus(AdoptionStatus.INTAKE_ASSESSMENT);
        animal.setIntakeDate(LocalDate.now());
        animal.setDescription("Max is a friendly dog");
        return animal;
    }

    @Test
    void shouldMapRequestDTOToEntity() {
        // ARRANGE
        AnimalRequestDTO request = buildTestRequestDTO();

        // ACT
        Animal animal = animalMapper.toEntity(request);

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
        AnimalRequestDTO request = buildTestRequestDTO();

        // ACT
        Animal animal = animalMapper.toEntity(request);

        // ASSERT — intakeDate should be set automatically to today
        assertThat(animal.getIntakeDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldSetStatusToIntakeAssessmentWhenMappingToEntity() {
        // ARRANGE
        AnimalRequestDTO request = buildTestRequestDTO();

        // ACT
        Animal animal = animalMapper.toEntity(request);

        // ASSERT — every new animal starts with INTAKE_ASSESSMENT
        assertThat(animal.getStatus()).isEqualTo(AdoptionStatus.INTAKE_ASSESSMENT);
    }

    @Test
    void shouldMapEntityToResponseDTO() {
        // ARRANGE
        Animal animal = buildTestAnimal();

        // ACT
        AnimalResponseDTO response = animalMapper.toResponse(animal);

        // ASSERT
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Max");
        assertThat(response.getSpecies()).isEqualTo("DOG");
        assertThat(response.getSex()).isEqualTo("MALE");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(2021, 5, 10));
        assertThat(response.getWeight()).isEqualTo(new BigDecimal("25.5"));
        assertThat(response.getSize()).isEqualTo("LARGE");
        assertThat(response.getVaccinationStatus()).isEqualTo("FULL_VACCINATED");
        assertThat(response.getSterilizationStatus()).isEqualTo("STERILIZED");
        assertThat(response.getAdoptionStatus()).isEqualTo("INTAKE_ASSESSMENT");
        assertThat(response.getDescription()).isEqualTo("Max is a friendly dog");
    }

    @Test
    void shouldConvertEnumsToStringsWhenMappingToResponseDTO() {
        // ARRANGE
        Animal animal = buildTestAnimal();

        // ACT
        AnimalResponseDTO response = animalMapper.toResponse(animal);

        // ASSERT — enums must be converted to String in the response
        assertThat(response.getSpecies()).isInstanceOf(String.class);
        assertThat(response.getSex()).isInstanceOf(String.class);
        assertThat(response.getSize()).isInstanceOf(String.class);
        assertThat(response.getVaccinationStatus()).isInstanceOf(String.class);
        assertThat(response.getSterilizationStatus()).isInstanceOf(String.class);
        assertThat(response.getAdoptionStatus()).isInstanceOf(String.class);
    }
}
