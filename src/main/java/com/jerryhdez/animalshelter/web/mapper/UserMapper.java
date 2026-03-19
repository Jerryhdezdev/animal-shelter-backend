package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Converts incoming request data (DTO) into a USER entity ready to be saved
    public User toEntity(UserRequestDTO dto){

        User user = new User();

        // Basic information
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());

        return user;
    }

    // Converts a USER entity into a response object to send back to the client
    public UserResponseDTO toResponse(User user){

        UserResponseDTO response = new UserResponseDTO();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setStatus(user.getStatus());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
