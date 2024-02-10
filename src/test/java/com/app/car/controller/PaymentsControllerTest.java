package com.app.car.controller;

import com.app.car.TestContainerManager;
import com.app.car.dto.payment.PaymentRequestDto;
import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.model.Car;
import com.app.car.model.Payment;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.model.enums.UserRole;
import com.app.car.model.enums.PaymentType;
import com.app.car.model.enums.PaymentStatus;
import com.app.car.repository.CarRepository;
import com.app.car.repository.PaymentRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.repository.UserRepository;
import com.app.car.security.AuthenticationService;
import com.app.car.service.PaymentService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
@DisplayName("PaymentsController Tests")
public class PaymentsControllerTest extends TestContainerManager {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authToken;
    private User user;
    private Car car;
    private Rental rental;
    private Payment payment;
    private PaymentRequestDto paymentRequest;

    @BeforeEach
    void init() {
        paymentRepository.deleteAll();
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();
        RestAssured.baseURI = "http://localhost:" + port + "/payments";

        user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_MANAGER)
                .build();

        userRepository.save(user);

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
                .actualReturnDate(null)
                .car(car)
                .user(user)
                .build();

        rentalRepository.save(rental);

        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto("test@example.com", "password");
        authToken = authenticationService.authenticate(loginRequestDto).token();

        paymentRequest = PaymentRequestDto.builder()
                .rentalId(1L)
                .paymentType(PaymentType.PAYMENT)
                .paymentAmount(new BigDecimal(50))
                .build();

        payment = Payment.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.PAYMENT)
                .rental(rental)
                .sessionUrl("yourSessionUrl")
                .sessionId("yourSessionId")
                .amountToPay(new BigDecimal("50.00"))
                .build();

        paymentRepository.save(payment);
    }

    @AfterEach
    void clear() {
        paymentRepository.deleteAll();
        rentalRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();
    }

//    @Test
//    @DisplayName("Get User Payments -> Valid Request")
//    void getUserPayments_ValidRequest_Success() {
//        given()
//                .contentType(ContentType.JSON)
//                .auth().oauth2(authToken)
//                .param("userId", user.getId())
//                .when()
//                .get()
//                .then()
//                .statusCode(200)
//                .body("$", notNullValue())
//                .body("size()", greaterThan(0));
//    }


//    @Test
//    @DisplayName("Create Payment Session -> Valid Request")
//    void createPaymentSession_ValidRequest_Success() {
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .auth().oauth2(authToken)
//                .body(paymentRequest)
//                .when()
//                .post();
//
//        response.then()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("status", is(PaymentStatus.PENDING.toString()))
//                .body("type", is(paymentRequest.getPaymentType().toString()))
//                .body("sessionUrl", notNullValue())
//                .body("sessionId", notNullValue())
//                .body("amountToPay", is(notNullValue(BigDecimal.class)));
//
//        System.out.println("Response body: " + response.getBody().asString());
//    }


//    @Test
//    @DisplayName("Check Successful Payments -> Valid Request")
//    void checkSuccessfulPayments_ValidRequest_Success() {
//        String successfulSessionId = "yourSessionId";
//
//        paymentService.handleSuccessfulPayment(payment.getSessionId());
//
//        given()
//                .contentType(ContentType.JSON)
//                .auth().oauth2(authToken)
//                .param("sessionId", successfulSessionId)
//                .when()
//                .get("/payments/success")
//                .then()
//                .statusCode(200)
//                .body(equalTo("Payment was successful!"));
//    }
//
//
//    @Test
//    @DisplayName("Return Payment Paused Message -> Valid Request")
//    void returnPaymentPausedMessage_ValidRequest_Success() {
//        String cancelledSessionId = "yourCancelledSessionId";
//
//        paymentService.handleCancelledPayment(cancelledSessionId);
//
//        given()
//                .contentType(ContentType.JSON)
//                .auth().oauth2(authToken)
//                .param("sessionId", cancelledSessionId)
//                .when()
//                .get("/payments/cancel")
//                .then()
//                .statusCode(200)
//                .body(equalTo("Payment has been cancelled!"));
//    }


}
