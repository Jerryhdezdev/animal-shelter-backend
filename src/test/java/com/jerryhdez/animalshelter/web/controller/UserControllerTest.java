package com.jerryhdez.animalshelter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.domain.service.UserService;
import com.jerryhdez.animalshelter.exception.UserNotFoundException;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;
import com.jerryhdez.animalshelter.web.mapper.UserMapper;

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

    @MockBean
    private UserMapper userMapper;

    // Helper method - builds a test response DTO to reuse across tests
    private UserResponseDTO buildTestResponseDTO() {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(1L);
        response.setFirstName("Jerry");
        response.setLastName("Hdez");
        response.setEmail("jerryhdez@example.com");
        response.setRole(UserRoles.ADOPTER);
        response.setStatus(UserStatus.PENDING);
        response.setCreatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        response.setUpdatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        return response;
    }

    // Helper method - builds a test request DTO to reuse across tests
    private UserRequestDTO buildTestRequestDTO() {
        UserRequestDTO request = new UserRequestDTO();
        request.setFirstName("Jerry");
        request.setLastName("Hdez");
        request.setEmail("jerryhdez@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        return request;
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        // ARRANGE
        UserResponseDTO response = buildTestResponseDTO();
        User user = new User();
        when(userService.getAllUsers()).thenReturn(List.of(user));
        when(userMapper.toResponse(any(User.class))).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jerry"))
                .andExpect(jsonPath("$[0].email").value("jerryhdez@example.com"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void shouldReturnUserById() throws Exception {
        // ARRANGE
        UserResponseDTO response = buildTestResponseDTO();
        User user = new User();
        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

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
    void shouldCreateUserSuccessfully() throws Exception {
        // ARRANGE
        UserRequestDTO request = buildTestRequestDTO();
        UserResponseDTO response = buildTestResponseDTO();
        User user = new User();

        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user);
        when(userService.saveUser(any(User.class), any(UserRequestDTO.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jerry"))
                .andExpect(jsonPath("$.email").value("jerryhdez@example.com"));
        verify(userService, times(1)).saveUser(any(User.class), any(UserRequestDTO.class));
    }

    @Test
    void shouldReturn400WhenPasswordsDoNotMatch() throws Exception {
        // ARRANGE
        UserRequestDTO request = buildTestRequestDTO();
        request.setConfirmPassword("differentPassword");
        User user = new User();

        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user);
        when(userService.saveUser(any(User.class), any(UserRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Passwords do not match"));

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).saveUser(any(User.class), any(UserRequestDTO.class));
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        // ARRANGE
        UserRequestDTO request = buildTestRequestDTO();
        UserResponseDTO response = buildTestResponseDTO();
        response.setFirstName("Jerry Updated");
        User user = new User();

        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user);
        when(userService.updateUser(eq(1L), any(User.class), any(UserRequestDTO.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jerry Updated"));
        verify(userService, times(1)).updateUser(eq(1L), any(User.class), any(UserRequestDTO.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
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