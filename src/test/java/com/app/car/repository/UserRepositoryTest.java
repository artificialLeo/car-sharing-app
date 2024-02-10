package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainerManager.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("findByEmail -> Existing Email")
    public void findByEmail_ExistingEmail_ReturnUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);

        // When
        User foundUser = userRepository.findByEmail("test@example.com").orElse(null);

        // Then
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("findByEmail -> Nonexistent Email")
    public void findByEmail_NonexistentEmail_ReturnNull() {
        // When
        User foundUser = userRepository.findByEmail("nonexistent@example.com").orElse(null);

        // Then
        assertNull(foundUser);
    }

    @Test
    @DisplayName("shouldGetAllCustomers -> Single User")
    public void shouldGetAllCustomers_SingleUser_ReturnMatchingEmail() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    @DisplayName("shouldGetAllCustomers -> Multiple Users")
    public void shouldGetAllCustomers_MultipleUsers_ReturnAllUsers() {
        // Given
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        userRepository.saveAll(List.of(user1, user2));

        // When
        List<User> allUsers = userRepository.findAll();

        // Then
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals(user1.getEmail())));
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals(user2.getEmail())));
    }
}

