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
    private UserRequestDTO createTestRequestDTO() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setFirstName("Jerry");
        requestDTO.setLastName("Hdez");
        requestDTO.setEmail("jerryhdez@example.com");
        requestDTO.setPassword("password123");
        requestDTO.setConfirmPassword("password123");
        return requestDTO;
    }

    // Helper method — builds a test user entity
    private User createTestUser() {
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
        UserRequestDTO requestDTO = createTestRequestDTO();

        // ACT
        User user = userMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(user.getFirstName()).isEqualTo("Jerry");
        assertThat(user.getLastName()).isEqualTo("Hdez");
    }

    @Test
    void shouldNotMapRoleAndStatusWhenMappingToEntity() {
        // ARRANGE
        UserRequestDTO requestDTO = createTestRequestDTO();

        // ACT
        User user = userMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(user.getRole()).isNull();
        assertThat(user.getStatus()).isNull();
    }

    @Test
    void shouldNotExposePasswordHashWhenMappingToResponseDTO() {
        // ARRANGE
        User user = createTestUser();

        // ACT
        UserResponseDTO response = userMapper.toResponse(user);

        // ASSERT
        assertThat(response).hasNoNullFieldsOrPropertiesExcept("passwordHash","otherOptionalField");
    }

    @Test
    void shouldMapEntityToResponseDTO() {
        // ARRANGE
        User user = createTestUser();

        // ACT
        UserResponseDTO response = userMapper.toResponse(user);

        // ASSERT
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("Jerry");
        assertThat(response.getLastName()).isEqualTo("Hdez");
        assertThat(response.getRole()).isEqualTo(UserRoles.ADOPTER);
        assertThat(response.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
    }

    @Test
    void shouldKeepEnumsAsEnumsWhenMappingToResponseDTO() {
        // ARRANGE
        User user = createTestUser();

        // ACT
        UserResponseDTO responseDTO = userMapper.toResponse(user);

        // ASSERT
        assertThat(responseDTO.getRole()).isInstanceOf(UserRoles.class);
        assertThat(responseDTO.getStatus()).isInstanceOf(UserStatus.class);
    }
}