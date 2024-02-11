package com.app.car.controller;

import com.app.car.config.TestContainerManager;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
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


//    @Test
//    @DisplayName("Create Payment Session -> Valid Request")
//    void createPaymentSession_ValidRequest_Success() {
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .auth().oauth2(authToken)
//                .body(paymentRequestDto)
//                .when()
//                .post();
//
//        response.then()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("status", is(PaymentStatus.PENDING.toString()))
//                .body("type", is(paymentRequestDto.getPaymentType().toString()))
//                .body("sessionUrl", notNullValue())
//                .body("sessionId", notNullValue())
//                .body("amountToPay", is(notNullValue(BigDecimal.class)));
//
//        System.out.println("Response body: " + response.getBody().asString());
//    }
//
//
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
