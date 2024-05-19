package com.test_task_rest_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_task_rest_api.controller.UserController;
import com.test_task_rest_api.exception.UserAlreadyExistsException;
import com.test_task_rest_api.model.User;
import com.test_task_rest_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class contains tests for the UserController class.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    /**
     * Sets up the test environment before each test method execution.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 5, 15));
    }

    /**
     * Test case for successful user creation.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testCreateUserSuccess() throws Exception {
        given(userService.createUser(any(User.class))).willReturn(user);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    /**
     * Test case for user creation when the user already exists.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testCreateUserAlreadyExists() throws Exception {
        given(userService.createUser(any(User.class))).willThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("User with the given email already exists.")));
    }

    /**
     * Test case for successful user update.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testUpdateUserSuccess() throws Exception {
        mockMvc.perform(put("/api/v1/users/john.doe@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User updated successfully.")));
    }

    /**
     * Test case for user update when the user is not found.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testUpdateUserNotFound() throws Exception {
        doThrow(new IllegalArgumentException("User not found")).when(userService).updateUser(eq("john.doe@example.com"), any(User.class));

        mockMvc.perform(put("/api/v1/users/john.doe@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));
    }

    /**
     * Test case for successful user deletion.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/users/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User was removed successfully.")));
    }

    /**
     * Test case for searching users by birth date range with successful results.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testSearchUsersByBirthDateRangeSuccess() throws Exception {
        List<User> users = Arrays.asList(user);
        given(userService.find(LocalDate.of(1990, 1, 1), LocalDate.of(2000, 12, 31))).willReturn(users);

        mockMvc.perform(get("/api/v1/users/search")
                        .param("from", "1990-01-01")
                        .param("to", "2000-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    /**
     * Test case for searching users by birthdate range with no results found.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testSearchUsersByBirthDateRangeNoResults() throws Exception {
        given(userService.find(LocalDate.of(1990, 1, 1), LocalDate.of(2000, 12, 31))).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/users/search")
                        .param("from", "1990-01-01")
                        .param("to", "2000-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
