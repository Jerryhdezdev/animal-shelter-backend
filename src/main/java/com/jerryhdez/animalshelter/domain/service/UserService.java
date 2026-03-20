package com.jerryhdez.animalshelter.domain.service;

import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.domain.enums.UserRoles;
import com.jerryhdez.animalshelter.domain.enums.UserStatus;
import com.jerryhdez.animalshelter.domain.repository.UserRepository;
import com.jerryhdez.animalshelter.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Gets all user from the database
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Retrieves a single user by id - throws exception is not found
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
    }

    // Saves a new user to the database
    public User saveUser(User user){

        // System automatically assigns default values
        user.setRole(UserRoles.ADOPTER);
        user.setStatus(UserStatus.PENDING);

        return userRepository.save(user);
    }

    // Updates an existing user - throws exception is not found
    public User updateUser(Long id, User updateUser){

        // First verifies if the user exists - Throws exceptions if not
        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));

        // Updates only the fields that are allowed to change
        existingUser.setFirstName(updateUser.getFirstName());
        existingUser.setLastName(updateUser.getLastName());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setRole(updateUser.getRole());
        existingUser.setStatus(updateUser.getStatus());

        // Saves and returns the user updated
        return userRepository.save(existingUser);
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
