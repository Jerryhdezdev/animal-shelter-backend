package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.enums.AdoptionStatus;
import com.jerryhdez.animalshelter.domain.model.Animal;
import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.domain.model.Adoption;
import com.jerryhdez.animalshelter.web.dto.AdoptionRequestDTO;
import com.jerryhdez.animalshelter.web.dto.AdoptionResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AdoptionMapper {

    // Converts incoming request data (DTO) into an Adoption entity ready to be saved
    public Adoption toEntity(AdoptionRequestDTO dto, User adopter, Animal animal){
        Adoption adoption = new Adoption();

         adoption.setAnimal(animal);
         adoption.setAdopter(adopter);
         adoption.setStatus(AdoptionStatus.REQUESTED);

        return adoption;
    }

    // Converts an Adoption entity into a response object to send back to the client
    public AdoptionResponseDTO toResponse(Adoption adoption){
        AdoptionResponseDTO response = new AdoptionResponseDTO();

        response.setId(adoption.getId());
        response.setAnimalName(adoption.getAnimal().getName());
        response.setUserName(adoption.getAdopter().getFirstName() + " " + adoption.getAdopter().getLastName());
        response.setAdoptionStatus(adoption.getStatus().toString());

        return response;

    }

}
