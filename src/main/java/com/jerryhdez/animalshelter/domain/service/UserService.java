package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;
import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.repository.UserRepository;
import com.jerryhdez.animalshelter.exception.UserNotFoundException;
import com.jerryhdez.animalshelter.web.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponseDTO getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    // Saves a new user to the database
    public UserResponseDTO createUser(UserRequestDTO request){


        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = userMapper.toEntity(request);
        user.setRole(UserRoles.ADOPTER);
        user.setStatus(UserStatus.PENDING);

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    // Updates an existing user - throws exception is not found
    public UserResponseDTO updateUser(Long id, UserRequestDTO request){

        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new IllegalArgumentException("Passwords do not match");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());

        User saved = userRepository.save(existingUser);
        return userMapper.toResponse(saved);
    }

    // Deletes an existing user - Throws exception if not found

    public void deleteUser(Long id){

        // First verifies if the user exists - throws exception if not
        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));

        // Deletes the user from the database
        userRepository.delete(existingUser);
    }
}
