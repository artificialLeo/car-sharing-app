package com.app.car.repository;

import com.app.car.config.TestContainerManager;
import com.app.car.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
class UserRepositoryTest extends TestContainerManager {
    @Test
    @DisplayName("Existing Email")
    public void findByEmail_ExistingEmail_ReturnUser() {
        User actual = userRepository.findByEmail(customer.getEmail()).orElse(null);

        assertNotNull(actual);
        assertEquals(customer.getEmail(), actual.getEmail());
        assertEquals(customer.getPassword(), actual.getPassword());
    }

    @Test
    @DisplayName("Nonexistent Email")
    public void findByEmail_NonexistentEmail_ReturnNull() {
        User actual = userRepository.findByEmail("nonexistent@example.com").orElse(null);

        assertNull(actual);
    }

    @Test
    @DisplayName("Single User")
    public void shouldGetAllCustomers_SingleUser_ReturnMatchingEmail() {
        User actual = userRepository.save(customer);

        assertEquals(customer.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Multiple Users")
    public void shouldGetAllCustomers_MultipleUsers_ReturnAllUsers() {
        List<User> allUsers = userRepository.findAll();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals(customer.getEmail())));
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals(manager.getEmail())));
    }
}
