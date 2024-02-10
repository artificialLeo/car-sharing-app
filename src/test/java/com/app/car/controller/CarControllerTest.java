package com.app.car.controller;

import com.app.car.TestContainerManager;
import com.app.car.dto.car.CarShortInfoDto;
import com.app.car.dto.car.CarUpdateDto;
import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.model.Car;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.model.enums.UserRole;
import com.app.car.repository.CarRepository;
import com.app.car.repository.UserRepository;
import com.app.car.security.AuthenticationService;
import com.app.car.security.JwtUtil;
import com.app.car.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.app.car.TestContainerManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
@DisplayName("CarController Tests")
public class CarControllerTest extends TestContainerManager {
    @LocalServerPort
    private Integer port;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private User manager;
    private Car car;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost:" + port + "/cars";

        manager = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_MANAGER)
                .build();

        car = Car.builder()
                .id(1L)
                .model("Toyota")
                .brand("Camry")
                .type(CarType.SEDAN)
                .inventory(15)
                .dailyFee(new BigDecimal("60.00"))
                .build();

        carRepository.save(car);
        userRepository.save(manager);

        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto("test@example.com", "password");
        authToken = authenticationService.authenticate(loginRequestDto).token();
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("Add Car -> Valid Request")
    void addCar_ValidRequest_Success() {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(authToken)
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
    @DisplayName("Get All Cars -> Authenticated User")
    void getAllCars_AuthenticatedUser_Success() {
        given()
                .auth().oauth2(authToken)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get Car By Id -> Authenticated User")
    void getCarById_AuthenticatedUser_Success() {
        given()
                .auth().oauth2(authToken)
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
    @DisplayName("Update Car -> Manager Role")
    void updateCar_ManagerRole_Success() {
        Long carId = 1L;
        given()
                .auth().oauth2(authToken)
                .contentType(ContentType.JSON)
                .body("{ \"inventory\": 15, \"dailyFee\": 60.00 }")
                .when()
                .put("/{id}", carId)
                .then()
                .statusCode(200)
                .body("inventory", equalTo(15))
                .body("dailyFee", equalTo(60.00f));
    }

    @Test
    @DisplayName("Delete Car -> Manager Role")
    void deleteCar_ManagerRole_Success() {
        Long carId = 1L;
        given()
                .auth().oauth2(authToken)
                .when()
                .delete("/{id}", carId)
                .then()
                .statusCode(200);
    }
}
