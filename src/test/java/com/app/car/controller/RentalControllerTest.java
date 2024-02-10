package com.app.car.controller;

import com.app.car.TestContainerManager;
import com.app.car.dto.rental.RentalDto;
import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.model.Car;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.model.enums.UserRole;
import com.app.car.repository.CarRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.repository.UserRepository;
import com.app.car.security.AuthenticationService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @LocalServerPort
    private Integer port;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private Rental rental;
    private Car car;
    private User user;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost:" + port + "/rentals";
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();

        user = User.builder()
                .email("testuser@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        userRepository.save(user);

        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto("testuser@example.com", "password");
        authToken = authenticationService.authenticate(loginRequestDto).token();


        car = Car.builder()
                .model("Toyota")
                .brand("Camry")
                .type(CarType.SEDAN)
                .inventory(10)
                .dailyFee(new BigDecimal("50.00"))
                .build();

        carRepository.save(car);


        rental = Rental.builder()
                .rentalDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .car(car)
                .user(user)
                .build();

        rentalRepository.save(rental);
    }

    @AfterEach
    void clear() {
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
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
                .userId(user.getId())
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
    void getRentalsByUserAndStatus_ValidRequest_Success() {

        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .param("userId", user.getId())
                .param("isActive", false)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
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

