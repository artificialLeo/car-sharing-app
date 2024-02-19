package com.app.car.controller;

import com.app.car.config.TestContainerManager;
import com.app.car.dto.rental.RentalDto;
import com.app.car.model.Car;
import com.app.car.model.enums.CarType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class RentalControllerTest extends TestContainerManager {

    @Test
    @DisplayName("Rental add")
    void addRental_ValidRentalRequest_Success() {
        Car car1 = Car.builder()
                .id(111L)
                .model("Toyota")
                .brand("Camry")
                .type(CarType.SEDAN)
                .inventory(10)
                .dailyFee(new BigDecimal("50.00"))
                .build();

        carRepository.save(car1);

        RentalDto rentalDto = RentalDto.builder()
                .rentalDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .carId(car1.getId())
                .userId(customer.getId())
                .build();

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .body(rentalDto)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("rentalDate", equalTo(rentalDto.getRentalDate().toString()))
                .body("returnDate", equalTo(rentalDto.getReturnDate().toString()));
    }

    @Test
    @DisplayName("Rentals by user and status")
    void getRentalsByUserAndStatus_ValidRequest_Success() {

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .param("userId", customer.getId())
                .param("isActive", false)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @DisplayName("Rentals by id - existing")
    void getRentalById_ExistingRentalId_Success() {
        Long rentalId = rental.getId();

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .pathParam("id", rentalId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(rentalId.intValue()));
    }

    @Test
    @DisplayName("Rentals by id - not existing")
    void getRentalById_NonExistingRentalId_NotFound() {
        Long nonExistingRentalId = 99L;

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .pathParam("id", nonExistingRentalId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Car return")
    void returnCar_ExistingRentalId_Success() {
        Long rentalId = rental.getId();

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .pathParam("rentalId", rentalId)
                .when()
                .post("/{rentalId}/return")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(rentalId.intValue()));
    }

    @Test
    @DisplayName("Car return - fail")
    void returnCar_NonExistingRentalId_NotFound() {
        Long nonExistingRentalId = 99L;

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .pathParam("rentalId", nonExistingRentalId)
                .when()
                .post("/{rentalId}/return")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}

