package com.jerryhdez.animalshelter.web.controller;


import com.jerryhdez.animalshelter.domain.model.User;
import com.jerryhdez.animalshelter.domain.service.UserService;
import com.jerryhdez.animalshelter.web.dto.UserRequestDTO;
import com.jerryhdez.animalshelter.web.dto.UserResponseDTO;
import com.jerryhdez.animalshelter.web.mapper.UserMapper;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Marks this class a REST controller
@RestController

// Base URL for all endpoints in this controller
@RequestMapping("/api/v1/users")
public class UserController {

    // Service and Mapper dependencies
    private final UserService userService;
    private final UserMapper userMapper;

    // Constructor injection - recommended over @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // GET /api/v1/users - retrieves all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return ResponseEntity.ok(users); // HTTP 200 OK

    }

    // GET /api/v1/users/{id} - retrieves a single user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        // Finds user - throws UserNotFoundException if not found
        User user = userService.getUserById(id);

        // Converts entity to response DTO
        UserResponseDTO response = userMapper.toResponse(user);

        return  ResponseEntity.ok(response); // HTTP 200 OK
    }

    // POST /api/v1/users - creates a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO request){

        // Converts request DTO to entity
        User user = userMapper.toEntity(request);

        // Saves entity to database
        User savedUser = userService.saveUser(user);

        // Converts saved entity back to response DTO
        UserResponseDTO response = userMapper.toResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201 CREATED
    }

    // PUT /api/v1/users/{id} - updates an existing user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request){

        // Converts request DTO to entity
        User updatedUser = userMapper.toEntity(request);

        // Updates user in database - throws UserNotFoundException if not found
        User savedUser = userService.updateUser(id, updatedUser);

        // Converts update entity to response DTO
        UserResponseDTO response = userMapper.toResponse(savedUser);

        return ResponseEntity.ok(response); //HTTP 200
    }

    // DELETE /api/v1/user/{id} - deletes an existing user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){

        // Deletes user - throws UserNotFoundException if not found
        userService.deleteUser(id);

        return ResponseEntity.noContent().build(); // HTTP 204 NO CONTENT
    }

}
