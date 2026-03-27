package com.jerryhdez.animalshelter.web.mapper;

import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    // No mocks needed — UserMapper has no dependencies
    private final UserMapper userMapper = new UserMapper();

    // Helper method — builds a test request DTO
    private UserRequestDTO buildTestRequestDTO() {
        UserRequestDTO request = new UserRequestDTO();
        request.setFirstName("Jerry");
        request.setLastName("Hdez");
        request.setEmail("jerryhdez@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        return request;
    }

    // Helper method — builds a test user entity
    private User buildTestUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jerry");
        user.setLastName("Hdez");
        user.setEmail("jerryhdez@example.com");
        user.setPasswordHash("password123");
        user.setRole(UserRoles.ADOPTER);
        user.setStatus(UserStatus.PENDING);
        user.setCreatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        user.setUpdatedAt(LocalDateTime.of(2026, 1, 1, 0, 0));
        return user;
    }

    @Test
    void shouldMapRequestDTOToEntity() {
        // ARRANGE
        UserRequestDTO request = buildTestRequestDTO();

        // ACT
        User user = userMapper.toEntity(request);

        // ASSERT
        assertThat(user.getFirstName()).isEqualTo("Jerry");
        assertThat(user.getLastName()).isEqualTo("Hdez");
        assertThat(user.getEmail()).isEqualTo("jerryhdez@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("password123");
    }

    @Test
    void shouldNotMapRoleAndStatusWhenMappingToEntity() {
        // ARRANGE
        UserRequestDTO request = buildTestRequestDTO();

        // ACT
        User user = userMapper.toEntity(request);

        // ASSERT — role and status are not set by the mapper, the service handles them
        assertThat(user.getRole()).isNull();
        assertThat(user.getStatus()).isNull();
    }

    @Test
    void shouldNotExposePasswordHashWhenMappingToResponseDTO() {
        // ARRANGE
        User user = buildTestUser();

        // ACT
        UserResponseDTO response = userMapper.toResponse(user);

        // ASSERT — password hash must never be exposed in the response
        assertThat(response).hasNoNullFieldsOrPropertiesExcept("passwordHash");
    }

    @Test
    void shouldMapEntityToResponseDTO() {
        // ARRANGE
        User user = buildTestUser();

        // ACT
        UserResponseDTO response = userMapper.toResponse(user);

        // ASSERT
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("Jerry");
        assertThat(response.getLastName()).isEqualTo("Hdez");
        assertThat(response.getEmail()).isEqualTo("jerryhdez@example.com");
        assertThat(response.getRole()).isEqualTo(UserRoles.ADOPTER);
        assertThat(response.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    @Test
    void shouldKeepEnumsAsEnumsWhenMappingToResponseDTO() {
        // ARRANGE
        User user = buildTestUser();

        // ACT
        UserResponseDTO response = userMapper.toResponse(user);

        // ASSERT — role and status stay as enums in the response (unlike Animal which converts to String)
        assertThat(response.getRole()).isInstanceOf(UserRoles.class);
        assertThat(response.getStatus()).isInstanceOf(UserStatus.class);
    }
}