package com.jerryhdez.animalshelter.exception;

// Custom exception thrown when user is not found in the database
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("User with id " + id + " not found");
  }
}
