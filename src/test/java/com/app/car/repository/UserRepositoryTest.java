package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.User;
import com.app.car.model.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainerManager.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;
    private User user2;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        user2 = User.builder()
                .email("user2@example.com")
                .firstName("Ann")
                .lastName("Hall")
                .password("password")
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        userRepository.saveAll(List.of(user, user2));
    }

    @AfterEach
    void destroy() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("findByEmail -> Existing Email")
    public void findByEmail_ExistingEmail_ReturnUser() {
        User foundUser = userRepository.findByEmail(user.getEmail()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("findByEmail -> Nonexistent Email")
    public void findByEmail_NonexistentEmail_ReturnNull() {
        User foundUser = userRepository.findByEmail("nonexistent@example.com").orElse(null);

        assertNull(foundUser);
    }

    @Test
    @DisplayName("shouldGetAllCustomers -> Single User")
    public void shouldGetAllCustomers_SingleUser_ReturnMatchingEmail() {
        User savedUser = userRepository.save(user);

        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    @DisplayName("shouldGetAllCustomers -> Multiple Users")
    public void shouldGetAllCustomers_MultipleUsers_ReturnAllUsers() {
        List<User> allUsers = userRepository.findAll();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals(user.getEmail())));
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals(user2.getEmail())));
    }
}
