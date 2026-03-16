package com.jerryhdez.animalshelter.web.dto;

import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    @NotBlank
    @Size(min = 8, max = 64)
    private String confirmPassword;

}
