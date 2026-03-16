package com.jerryhdez.animalshelter.web.dto;

import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.enums.UserRoles;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {

    // Unique identifier assigned by the database
    private Long id;

    // Basic information
    private String firstName;
    private String lastName;

    // Login Credential
    private String email;

    // Role and status
    private UserRoles role;
    private UserStatus status;

    // Information for Audit purpose
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
