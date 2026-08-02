package com.jerryhdez.animalshelter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.service.UserService;
import com.jerryhdez.animalshelter.exception.UserNotFoundException;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    // Helper method - builds a test response DTO to reuse across tests
    private UserResponseDTO createTestResponseDTO() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setFirstName("Jerry");
        responseDTO.setLastName("Hdez");
        responseDTO.setEmail("jerryhdez@example.com");
        responseDTO.setRole(UserRoles.ADOPTER);
        responseDTO.setStatus(UserStatus.PENDING);
        responseDTO.setCreatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        responseDTO.setUpdatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        return responseDTO;
    }

    // Helper method - builds a test request DTO to reuse across tests
    private UserRequestDTO createTestRequestDTO() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setFirstName("Jerry");
        requestDTO.setLastName("Hdez");
        requestDTO.setEmail("jerryhdez@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setConfirmPassword("password123");
        return requestDTO;
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        // ARRANGE
        UserResponseDTO responseDTO = createTestResponseDTO();
        when(userService.getAllUsers()).thenReturn(List.of(responseDTO));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jerry"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void shouldReturnUserById() throws Exception {
        // ARRANGE
        UserResponseDTO responseDTO = createTestResponseDTO();
        when(userService.getUserById(1L)).thenReturn(responseDTO);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jerry"));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        // ARRANGE
        when(userService.getUserById(13L))
                .thenThrow(new UserNotFoundException(13L));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/users/13"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
        verify(userService, times(1)).getUserById(13L);
    }

    @Test
    void shouldCreateUser() throws Exception {
        // ARRANGE
        UserRequestDTO requestDTO = createTestRequestDTO();
        UserResponseDTO responseDTO = createTestResponseDTO();
        doReturn(responseDTO).when(userService).createUser(any(UserRequestDTO.class));

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jerry"));
        verify(userService, times(1)).createUser(any(UserRequestDTO.class));
    }

    @Test
    void shouldReturn400WhenPasswordsDoNotMatch() throws Exception {
        // ARRANGE
        UserRequestDTO requestDTO = createTestRequestDTO();
        requestDTO.setConfirmPassword("differentPassword");


        doThrow(new IllegalArgumentException("Passwords do not match"))
                .when(userService).createUser(any(UserRequestDTO.class));

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).createUser(any(UserRequestDTO.class));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // ARRANGE
        UserRequestDTO requestDTO = createTestRequestDTO();
        UserResponseDTO responseDTO = createTestResponseDTO();
        responseDTO.setFirstName("Jerry Updated");

        doReturn(responseDTO).when(userService).updateUser(eq(1L), any(UserRequestDTO.class));

        // ACT + ASSERT
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jerry Updated"));
        verify(userService, times(1)).updateUser(eq(1L), any(UserRequestDTO.class));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // ARRANGE
        doNothing().when(userService).deleteUser(1L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentUser() throws Exception {
        // ARRANGE
        doThrow(new UserNotFoundException(13L))
                .when(userService).deleteUser(13L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/users/13"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
        verify(userService, times(1)).deleteUser(13L);
    }
}