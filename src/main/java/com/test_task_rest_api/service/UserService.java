package com.test_task_rest_api.service;

import com.test_task_rest_api.model.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {
    /**
     * Creates user
     * @param user - user want to be created
     * @return - created user
     */
    User createUser(User user);


    /**
     * Updates user
     * @param email - new e-mail value
     * @param user - user, value we want update
     * @return - updated user
     */
    User updateUser(String email, User user);



    /**
     * Deletes user
     * @param email - parameter that marks user for deletion
     */
    void deleteUser(String email);


    /**
     * Search for users by birthdate range.
     * @param birthDateFrom - start date
     * @param birthDateTo - end date
     * @return list users by birthdate range
     */
    List<User> find(LocalDate birthDateFrom, LocalDate birthDateTo);
}
