package com.app.car;

import com.app.car.model.User;
import com.app.car.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Container
	CustomMySqlContainer customMySqlContainer;

	@Test
	void contextLoads() {
		customMySqlContainer.start();

		userRepository.save(new User());
	}

}
