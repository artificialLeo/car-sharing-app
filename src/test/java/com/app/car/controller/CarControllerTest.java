package com.app.car.controller;

import com.app.car.config.TestContainerManager;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
@DisplayName("CarController Tests")
public class CarControllerTest extends TestContainerManager {
    @Test
    @DisplayName("Add car")
    void addCar_ValidRequest_Success() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(authTokenManager)
                .body("{ \"model\": \"Toyota\", \"brand\": \"Camry\", \"type\": \"SEDAN\", \"inventory\": 10, \"dailyFee\": 50.00 }")
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("model", equalTo("Toyota"))
                .body("brand", equalTo("Camry"))
                .body("type", equalTo("SEDAN"))
                .body("inventory", equalTo(10))
                .body("dailyFee", equalTo(50.00f));
    }

    @Test
    @DisplayName("Get all cars")
    void getAllCars_AuthenticatedUser_Success() {
        given()
                .auth().oauth2(authTokenManager)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get car by id")
    void getCarById_AuthenticatedUser_Success() {
        given()
                .auth().oauth2(authTokenManager)
                .when()
                .get("/{id}", car.getId())
                .then()
                .statusCode(200)
                .body("model", is(notNullValue()))
                .body("brand", is(notNullValue()))
                .body("type", is(notNullValue()))
                .body("inventory", is(notNullValue()))
                .body("dailyFee", is(notNullValue()));
    }

    @Test
    @DisplayName("Car update")
    void updateCar_ManagerRole_Success() {
        given()
                .auth().oauth2(authTokenManager)
                .contentType(ContentType.JSON)
                .body("{ "
                        + "\"model\": \"Subaru\", "
                        + "\"brand\": \"Impreza\", "
                        + "\"type\": \"SEDAN\", "
                        + "\"dailyFee\": \"20\""
                        + "}"
                )
                .when()
                .put("/{id}", car.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(car.getId().intValue()))
                .body("model", equalTo("Subaru"))
                .body("brand", equalTo("Impreza"))
                .body("type", equalTo("SEDAN"))
                .body("dailyFee", equalTo(20));
    }



    @Test
    @DisplayName("Car delete")
    void deleteCar_ManagerRole_Success() {
        given()
                .auth().oauth2(authTokenManager)
                .when()
                .delete("/{id}", deletedCar.getId())
                .then()
                .statusCode(200);
    }
}
