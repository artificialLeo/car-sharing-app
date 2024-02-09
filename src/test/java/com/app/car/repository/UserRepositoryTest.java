package com.app.car.repository;

import com.app.car.TestContainerManager;
import com.app.car.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

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
    void shouldGetAllCustomers() {
        User user = new User();

        user.setEmail("email");

        User savedUser = userRepository.save(user);

        Assertions.assertEquals(user.getEmail(), savedUser.getEmail());
    }
}

