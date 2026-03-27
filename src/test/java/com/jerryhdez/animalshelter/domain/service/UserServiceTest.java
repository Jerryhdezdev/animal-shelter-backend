package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
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

    @InjectMocks
    private UserService userService;

    // Helper method - builds a test user to reuse across tests
    private User buildTestUser(){
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
    private UserRequestDTO buildTestDTO(){
        UserRequestDTO dto = new UserRequestDTO();

        dto.setPassword("password123");
        dto.setConfirmPassword("password123");
        return dto;
    }

    @Test
    void shouldReturnAllUser() {
        // ARRANGE
        User user = buildTestUser();
        when(userRepository.findAll()).thenReturn(List.of(user));

        // ACT
        List<User> result = userService.getAllUsers();

        // ASSERT
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("jerryhdez@example.com");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserWhenIdExists(){
        // ARRANGE
        User user = buildTestUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // ACT
        User result = userService.getUserById(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("jerryhdez@example.com");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        // ARRANGE
        when(userRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> userService.getUserById(13L))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(13L);
    }

    @Test
    void shouldSaveUserSuccessfully(){
        // ARRANGE
        User user = buildTestUser();
        UserRequestDTO dto = buildTestDTO();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // ACT
        User result = userService.saveUser(user,dto);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("jerryhdez@example.com");
        assertThat(result.getRole()).isEqualTo(UserRoles.ADOPTER);
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatchOnSave(){
        // ARRANGE
        User user = buildTestUser();
        UserRequestDTO dto = buildTestDTO();
        dto.setConfirmPassword("differentPassword");

        // ACT + ASSERT
        assertThatThrownBy(()-> userService.saveUser(user,dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passwords do not match");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateUserSuccessfully(){
        // ARRANGE
        User existingUser = buildTestUser();
        User updatedUser = buildTestUser();
        updatedUser.setFirstName("Jerry Updated");
        updatedUser.setEmail("jerryhdez.updated@example.com");
        UserRequestDTO dto = buildTestDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // ACT
        User result = userService.updateUser(1L, updatedUser, dto);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jerry Updated");
        assertThat(result.getEmail()).isEqualTo("jerryhdez.updated@example.com");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentUser(){
        // ARRANGE
        User updatedUser = buildTestUser();
        UserRequestDTO dto = buildTestDTO();
        when(userRepository.findById(13L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(()-> userService.updateUser(13L, updatedUser, dto))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(13L);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatchOnUpdate(){
        // ARRANGE
        User existingUser = buildTestUser();
        User updatedUser = buildTestUser();
        UserRequestDTO dto = buildTestDTO();
        dto.setConfirmPassword("differentPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // ACT + ASSERT
        assertThatThrownBy(()-> userService.updateUser(1L, updatedUser, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passwords do not match");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully(){
        // ARRANGE
        User user = buildTestUser();
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
