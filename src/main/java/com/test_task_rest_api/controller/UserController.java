package com.test_task_rest_api.controller;

import com.test_task_rest_api.exception.UserAlreadyExistsException;
import com.test_task_rest_api.exception.UserNotFoundException;
import com.test_task_rest_api.model.User;
import com.test_task_rest_api.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
@Log4j2
public class UserController {
    private final UserService userService;

    @Value("${user.minAge}")
    private int minAge;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Creating user: {}", user);
        User createdUser = userService.createUser(user);

        log.info("User created: {}", createdUser);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Updates an existing user.
     *
     * @param email the email of the user to update
     * @param user the user details to update
     * @return the updated user
     */
    @PutMapping("/{email}")
    public ResponseEntity<String> updateUser(@PathVariable String email, @RequestBody User user) {
        try {
            userService.updateUser(email, user);
            log.info("User {} updated successfully.", user);
            return ResponseEntity.ok("User updated successfully.");
        } catch (IllegalArgumentException e) {
            log.error("Could not update new user. {}.", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    /**
     * Deletes a user.
     *
     * @param email the email of the user to delete
     * @return response entity
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);

        log.info("User with e-mail {} removed.", email);

        return ResponseEntity.status(200).body("User was removed successfully.");
    }


    /**
     * Searches for users by birthdate range.
     *
     * @param from the start date of the range
     * @param to the end date of the range
     * @return the list of users within the specified date range
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(@RequestParam LocalDate from,
                                                                  @RequestParam LocalDate to) {
        List<User> userList = userService.find(from, to);
        log.info("Requested users with birthdays between {} and {}. Found {} user(s).", from, to, userList.size());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a simple error message.
     *
     * @param ex The exception to handle
     * @return ResponseEntity containing a simple error message and HTTP status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();

        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> errorMessage.append(error.getDefaultMessage()).append(". "));

        log.error("Validation failed: {}", errorMessage.toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Validation failed: " + errorMessage.toString());
    }

    /**
     * Exception handler for UserNotFoundException. Returns a 500 status code and a message indicating
     * that the requested user was not found.
     *
     * @param ex The UserNotFoundException instance
     * @return ResponseEntity containing a 500 status code and a message indicating user not found
     */
    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Could not find user.");
        return ResponseEntity.status(500).body("User with requested email does not exist.");
    }

    /**
     * Exception handler for UserAlreadyExistsException. Returns a 409 status code and a message indicating
     * that a user with the given email already exists.
     *
     * @param ex The UserAlreadyExistsException instance
     * @return ResponseEntity containing a 409 status code and a message indicating user already exists
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("User already exists: {}", ex.getMessage());
        return ResponseEntity.status(409).body("User with the given email already exists.");
    }

    /**
     * Exception handler for IllegalArgumentException. Returns a 400 status code and the message
     * provided by the IllegalArgumentException.
     *
     * @param ex The IllegalArgumentException instance
     * @return ResponseEntity containing a 400 status code and the message from the IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Validation error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
