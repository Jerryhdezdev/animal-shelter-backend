package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;
import com.jerryhdez.animalshelter.web.mapper.UserMapper;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.repository.UserRepository;
import com.jerryhdez.animalshelter.exception.UserNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    // Helper method - builds a test user to reuse across tests
    private User createTestUser(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jerry");
        user.setLastName("Hdez");
        user.setEmail("jerryhdez@example.com");
        user.setPasswordHash("hashed_password");
        user.setRole(UserRoles.ADOPTER);
        user.setStatus(UserStatus.PENDING);
        return user;
    }

    // Helper method - builds a test DTO to reuse across tests
    private UserRequestDTO createTestUserRequestDTO(){
        UserRequestDTO requestDTO = new UserRequestDTO();

        requestDTO.setPassword("password123");
        requestDTO.setConfirmPassword("password123");
        return requestDTO;
    }

    private UserResponseDTO createTestUserResponseDTO(){
        UserResponseDTO responseDTO = new UserResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setFirstName("Jerry");

        return responseDTO;
    }

    @Test
    void shouldReturnAllUser() {
        // ARRANGE
        User user = createTestUser();
        UserResponseDTO responseDTO = createTestUserResponseDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(responseDTO);

        // ACT
        List<UserResponseDTO> result = userService.getAllUsers();

        // ASSERT
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jerry");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserWhenIdExists(){
        // ARRANGE
        User user = createTestUser();
        UserResponseDTO responseDTO = createTestUserResponseDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(responseDTO);

        // ACT
        UserResponseDTO result = userService.getUserById(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Jerry");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        // ARRANGE
        when(userRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> userService.getUserById(13L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id 13 not found");
        verify(userRepository, times(1)).findById(13L);
    }

    @Test
    void shouldCreateUser(){
        // ARRANGE
        UserRequestDTO requestDTO = createTestUserRequestDTO();
        requestDTO.setFirstName("Jerry");

        User user = createTestUser();
        UserResponseDTO responseDTO = createTestUserResponseDTO();

        when(userMapper.toEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(responseDTO);

        // ACT
        UserResponseDTO result = userService.createUser(requestDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jerry");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatchOnSave(){
        // ARRANGE
        UserRequestDTO requestDTO = createTestUserRequestDTO();
        requestDTO.setConfirmPassword("differentPassword");

        // ACT + ASSERT
        assertThatThrownBy(()-> userService.createUser(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passwords do not match");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateUser(){
        // ARRANGE
        User existingUser = createTestUser();
        User updatedUser = createTestUser();
        UserResponseDTO responseDTO = createTestUserResponseDTO();
        UserRequestDTO requestDTO = createTestUserRequestDTO();
        responseDTO.setFirstName("Jerry Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(responseDTO);

        // ACT
        UserResponseDTO result = userService.updateUser(1L, requestDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jerry Updated");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentUser(){
        // ARRANGE
        UserRequestDTO requestDTO = createTestUserRequestDTO();
        when(userRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(()-> userService.updateUser(13L, requestDTO))
                .isInstanceOf(UserNotFoundException.class)
                        .hasMessageContaining("User with id 13 not found");
        verify(userRepository, times(1)).findById(13L);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatchOnUpdate(){
        // ARRANGE
        UserRequestDTO requestDTO = createTestUserRequestDTO();
        requestDTO.setConfirmPassword("differentPassword");

        // ACT + ASSERT
        assertThatThrownBy(() -> userService.updateUser(1L, requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passwords do not match");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully(){
        // ARRANGE
        User user = createTestUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // ACT
        userService.deleteUser(1L);

        // ASSERT
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentUser(){
        // ARRANGE
        when(userRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(()-> userService.deleteUser(13L))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(13L);
    }
}
