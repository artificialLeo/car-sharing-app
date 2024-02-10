package com.app.car.controller;

import com.app.car.TestContainerManager;
import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.exception.RegistrationException;
import com.app.car.model.User;
import com.app.car.model.enums.UserRole;
import com.app.car.repository.UserRepository;
import com.app.car.security.AuthenticationService;
import com.app.car.service.UserService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class AuthControllerTest extends TestContainerManager {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRegistrationRequestDto registrationRequestDto;
    private UserLoginRequestDto loginRequestDto;
    private User userForLogin;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost:" + port + "/auth";
        userRepository.deleteAll();

        registrationRequestDto = new UserRegistrationRequestDto();
        registrationRequestDto.setEmail("test@example.com");
        registrationRequestDto.setFirstName("John");
        registrationRequestDto.setLastName("Doe");
        registrationRequestDto.setPassword("password");
        registrationRequestDto.setRepeatPassword("password");

        loginRequestDto = new UserLoginRequestDto("smail@example.com", "password");

        userForLogin = User.builder()
                .email("smail@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        userRepository.save(userForLogin);
    }

    @Test
    void registration_ValidRegistrationRequest_Success() throws RegistrationException {
        Response response = given()
                .contentType("application/json")
                .body(registrationRequestDto)
                .when()
                .post("/registration")
                .andReturn();

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo("test@example.com"));
    }

    @Test
    void login_ValidLoginRequest_Success() {
        given()
                .contentType("application/json")
                .body(loginRequestDto)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", is(notNullValue()));
    }
}

