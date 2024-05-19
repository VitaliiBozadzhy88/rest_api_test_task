package com.test_task_rest_api.service.impl;

import com.test_task_rest_api.exception.UserAlreadyExistsException;
import com.test_task_rest_api.exception.UserNotFoundException;
import com.test_task_rest_api.model.User;
import com.test_task_rest_api.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {
    @Value("${user.minAge}")
    private int minAge;

    //mocks database
    private final Map<String, User> userRepository = new HashMap<>();


    /**
     * Save user
     * @param user - user want to be created
     * @return created user
     */
    @Override
    public User createUser(User user) {
        if (userRepository.containsKey(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        if (calculateAge(user.getBirthDate()) < minAge) {
            throw new IllegalArgumentException("User must be at least " + minAge + " years old");
        }
        userRepository.put(user.getEmail(), user);

        return user;
    }

    /**
     * Update user by his e-mail
     * @param email - new e-mail value
     * @param user - user, value we want update
     * @return updated user
     */
    @Override
    public User updateUser(String email, User user) {
        if (!userRepository.containsKey(email)) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        userRepository.put(email, user);

        return user;
    }


    /**
     * Remove user
     * @param email - parameter that marks user for deletion
     */
    @Override
    public void deleteUser(String email) {
        if (!userRepository.containsKey(email)) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        userRepository.remove(email);
    }

    /**
     * Find users between dates
     * @param birthDateFrom - start date
     * @param birthDateTo - end date
     * @return a list of users
     */
    @Override
    public List<User> find(LocalDate birthDateFrom, LocalDate birthDateTo) {
        if (birthDateFrom.isAfter(birthDateTo)) {
            throw new IllegalArgumentException("From date must be before To date");
        }

        List<User> usersInRange = new ArrayList<>();

        for (User user : userRepository.values()) {
            if ((user.getBirthDate().isEqual(birthDateFrom) || user.getBirthDate().isAfter(birthDateFrom))
                    && (user.getBirthDate().isEqual(birthDateTo) || user.getBirthDate().isBefore(birthDateTo))) {
                usersInRange.add(user);
            }
        }
        return usersInRange;
    }


    /**
     * Calculates the age of the user based on the birthdate.
     *
     * @param birthDate the birthdate of the user
     * @return the age of the user
     */
    private int calculateAge(LocalDate birthDate) {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}
