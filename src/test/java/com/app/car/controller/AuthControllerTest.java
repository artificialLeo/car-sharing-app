package com.app.car.controller;

import com.app.car.config.TestContainerManager;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class AuthControllerTest extends TestContainerManager {
    @Test
    @DisplayName("User register")
    void registration_ValidRegistrationRequest_Success() {
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
    @DisplayName("Log in with JWT token")
    void login_ValidLoginRequest_Success() {
        given()
                .contentType("application/json")
                .body(customerLoginRequestDto)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", is(notNullValue()));
    }
}
