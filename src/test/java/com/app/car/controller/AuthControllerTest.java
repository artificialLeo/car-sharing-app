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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class AuthControllerTest extends TestContainerManager {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost:" + port + "/auth";
        userRepository.deleteAll();
    }

    @Test
    void registration_ValidRegistrationRequest_Success() throws RegistrationException {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setPassword("password");
        requestDto.setRepeatPassword("password");

        Response response = given()
                .contentType("application/json")
                .body(requestDto)
                .when()
                .post("/registration")
                .andReturn();

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo("test@example.com"))
                .body("id", equalTo(1));

    }


    @Test
    void login_ValidLoginRequest_Success() {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("smail@example.com", "password");
        User user = User.builder()
                .email("smail@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_CUSTOMER)
                .build();

        userRepository.save(user);

        System.out.println(userRepository.findByEmail("smail@example.com"));

        given()
                .contentType("application/json")
                .body(requestDto)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value());
//                .body("token", equalTo("your_expected_token_here"));
    }
}

