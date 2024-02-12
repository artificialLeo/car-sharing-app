package com.app.car.controller;

import com.app.car.config.TestContainerManager;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
@DisplayName("PaymentsController Tests")
public class PaymentsControllerTest extends TestContainerManager {

    @Test
    @DisplayName("User payments")
    void getUserPayments_ValidRequest_Success() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(authToken)
                .param("userId", customer.getId())
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("$", notNullValue())
                .body("size()", greaterThan(0));
    }

}
