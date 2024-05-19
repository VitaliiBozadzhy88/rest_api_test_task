package com.test_task_rest_api.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents a User in the system.
 */
@Data
public class User {
    /**
     * The email of the user.
     */
    @Email(message = "Email should be valid.")
    @NotNull(message = "Email is required.")
    private String email;

    /**
     * The first name of the user.
     */
    @NotNull(message = "First name is required.")
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotNull(message = "Last name is required.")
    private String lastName;

    /**
     * The birthdate of the user.
     */
    @NotNull(message = "Birth date is required.")
    @Past(message = "Birth date must be in the past.")
    private LocalDate birthDate;

    /**
     * The address of the user.
     */
    private String address;

    /**
     * The phone number of the user.
     */
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid.")
    private String phoneNumber;

    // toString for better logging
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
