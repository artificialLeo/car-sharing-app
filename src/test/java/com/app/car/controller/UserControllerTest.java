package com.app.car.controller;

import com.app.car.TestContainerManager;
import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.model.User;
import com.app.car.model.enums.UserRole;
import com.app.car.repository.UserRepository;
import com.app.car.security.AuthenticationService;
import io.restassured.RestAssured;
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
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class UserControllerTest extends TestContainerManager {

    @LocalServerPort
    private Integer port;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private User user;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost:" + port + "/user";
        userRepository.deleteAll();

        user = User.builder()
                .email("testuser@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        userRepository.save(user);

        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto("testuser@example.com", "password");
        authToken = authenticationService.authenticate(loginRequestDto).token();
    }

    @Test
    void updateUserRole_ManagerRole_Success() {
        User managerUser = User.builder()
                .email("manager@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_MANAGER)
                .build();
        userRepository.save(managerUser);

        UserLoginRequestDto managerLoginRequestDto = new UserLoginRequestDto("manager@example.com", "password");
        String managerAuthToken = authenticationService.authenticate(managerLoginRequestDto).token();

        Long userId = user.getId();
        UserRole newRole = UserRole.ROLE_CUSTOMER;

        given()
                .contentType("application/json")
                .auth().oauth2(managerAuthToken)
                .pathParam("id", userId)
                .param("newRole", newRole)
                .when()
                .put("/{id}/role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    void getMyProfileInfo_ValidRequest_Success() {
        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .when()
                .get("/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("email", equalTo("testuser@example.com"))
                .body("role", equalTo(UserRole.ROLE_CUSTOMER.toString()));
    }

    @Test
    void updateMyProfileInfo_ValidProfileUpdateRequest_Success() {
        UpdateUserProfileDto updatedUserProfile = new UpdateUserProfileDto(
                "UpdatedFirstName",
                "UpdatedLastName",
                "mail.example.com",
                "password",
                "password"

        );


        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .body(updatedUserProfile)
                .when()
                .put("/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", equalTo("UpdatedFirstName"))
                .body("lastName", equalTo("UpdatedLastName"));
    }
}
