package com.app.car.controller;

import com.app.car.config.TestContainerManager;
import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.model.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainerManager.class)
public class UserControllerTest extends TestContainerManager {

    @Test
    @DisplayName("Role update")
    void updateUserRole_ManagerRole_Success() {
        given()
                .contentType("application/json")
                .auth().oauth2(authTokenManager)
                .pathParam("id", customer.getId())
                .param("newRole", UserRole.ROLE_MANAGER)
                .when()
                .put("/{id}/role")
                .then()
                .statusCode(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("Profile get")
    void getMyProfileInfo_ValidRequest_Success() {
        given()
                .contentType("application/json")
                .auth().oauth2(authToken)
                .when()
                .get("/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("email", equalTo("customer@example.com"))
                .body("role", equalTo(UserRole.ROLE_CUSTOMER.toString()));
    }

    @Test
    @DisplayName("Profile update")
    void updateMyProfileInfo_ValidProfileUpdateRequest_Success() {
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
