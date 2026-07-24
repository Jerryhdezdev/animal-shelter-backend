package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.model.Adoption;
import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.web.dto.AdoptionResponseDTO;
import com.jerryhdez.animalshelter.web.dto.AdoptionRequestDTO;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AdoptionMapperTest {

    // No mocks needed - AdoptionMapper has not dependencies
    private final AdoptionMapper adoptionMapper = new AdoptionMapper();

    // Helper methods
private Animal createTestAnimal(){
    Animal animal = new Animal();
    animal.setName("Fiona");
    return animal;
}

private User createTestUser(){
    User user = new User();
    user.setFirstName("Jerry");
    user.setLastName("Hdez");
    return user;
}

    private AdoptionRequestDTO createTestRequestDTO() {
        AdoptionRequestDTO dto = new AdoptionRequestDTO();
        dto.setAnimalId(1L);
        return dto;
    }

private Adoption createTestAdoption(){
    Adoption adoption = new Adoption();
    adoption.setId(1L);
    adoption.setAnimal(createTestAnimal());
    adoption.setAdopter(createTestUser());
    adoption.setStatus(AdoptionStatus.REQUESTED);
    return adoption;
}


    @Test
void shouldConvertRequestToEntity(){
    // ARRANGE
        AdoptionRequestDTO requestDTO = createTestRequestDTO();
        User user = createTestUser();
        Animal animal = createTestAnimal();

        // ACT
        Adoption adoption = adoptionMapper.toEntity(requestDTO, user, animal);

        // ASSERT
        assertThat(adoption).isNotNull();
        assertThat(adoption.getAnimal()).isEqualTo(animal);
        assertThat(adoption.getAdopter()).isEqualTo(user);
        assertThat(adoption.getStatus()).isEqualTo(AdoptionStatus.REQUESTED);


    }

    @Test
    void shouldConvertEntityToResponse(){
    // ARRANGE
        Adoption adoption = createTestAdoption();

        // ACT
        AdoptionResponseDTO responseDTO = adoptionMapper.toResponse(adoption);

        // ASSERT
        assertThat(responseDTO.getAnimalName()).isEqualTo("Fiona");
        assertThat(responseDTO.getUserName()).isEqualTo("Jerry Hdez");
        assertThat(responseDTO.getAdoptionStatus()).isEqualTo("REQUESTED");

    }

}
