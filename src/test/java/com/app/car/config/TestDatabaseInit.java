package com.app.car.config;

import com.app.car.dto.payment.PaymentRequestDto;
import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.model.Car;
import com.app.car.model.Payment;
import com.app.car.model.Rental;
import com.app.car.model.User;
import com.app.car.model.enums.CarType;
import com.app.car.model.enums.PaymentStatus;
import com.app.car.model.enums.PaymentType;
import com.app.car.model.enums.UserRole;
import com.app.car.repository.CarRepository;
import com.app.car.repository.PaymentRepository;
import com.app.car.repository.RentalRepository;
import com.app.car.repository.UserRepository;
import com.app.car.security.AuthenticationService;
import com.app.car.service.PaymentService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class TestDatabaseInit {
    @LocalServerPort
    protected Integer port;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CarRepository carRepository;

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected RentalRepository rentalRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected PaymentService paymentService;

    @Autowired
    private AuthenticationService authenticationService;

    protected String authToken;
    protected String authTokenManager;
    protected UserRegistrationRequestDto registrationRequestDto;
    protected UpdateUserProfileDto updatedUserProfile;
    protected UserLoginRequestDto customerLoginRequestDto;
    protected UserLoginRequestDto managerLoginRequestDto;
    protected User manager;
    protected User customer;
    protected Car car;
    protected Car deletedCar;
    protected Rental rental;
    protected Payment payment;
    protected PaymentRequestDto paymentRequestDto;

    @BeforeEach
    void initData() {
        setBaseURI(getClass(), port);

        manager = User.builder()
                .email("manager@example.com")
                .firstName("John")
                .lastName("Manager")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_MANAGER)
                .build();
        customer = User.builder()
                .email("customer@example.com")
                .firstName("Anny")
                .lastName("Customer")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        userRepository.saveAll(List.of(customer, manager));

        registrationRequestDto = UserRegistrationRequestDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .repeatPassword("password")
                .build();
        customerLoginRequestDto = new UserLoginRequestDto("customer@example.com", "password");
        authToken = authenticationService.authenticate(customerLoginRequestDto).token();
        managerLoginRequestDto = new UserLoginRequestDto("manager@example.com", "password");
        authTokenManager = authenticationService.authenticate(managerLoginRequestDto).token();

        updatedUserProfile = new UpdateUserProfileDto(
                "UpdatedFirstName",
                "UpdatedLastName",
                "mail.example.com",
                "password",
                "password"

        );


        car = Car.builder()
                .id(1L)
                .model("Toyota")
                .brand("Camry")
                .type(CarType.SEDAN)
                .inventory(15)
                .dailyFee(new BigDecimal("60.00"))
                .build();
        deletedCar = Car.builder()
                .id(1L)
                .model("Toyota")
                .brand("Camry")
                .type(CarType.SEDAN)
                .inventory(1)
                .dailyFee(new BigDecimal("60.00"))
                .build();
        carRepository.saveAll(List.of(car, deletedCar));


        rental = Rental.builder()
                .rentalDate(LocalDate.now())
                .returnDate(LocalDate.now().minusDays(5))
                .car(car)
                .user(customer)
                .build();
        rentalRepository.save(rental);

        payment = Payment.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.PAYMENT)
                .rental(rental)
                .sessionUrl("yourSessionUrl")
                .sessionId("yourSessionId")
                .amountToPay(new BigDecimal("50.00"))
                .build();
        paymentRepository.save(payment);

        paymentRequestDto = PaymentRequestDto.builder()
                .rentalId(1L)
                .paymentType(PaymentType.PAYMENT)
                .paymentAmount(new BigDecimal(50))
                .build();

    }

    @AfterEach
    void cleanupData() {
        paymentRepository.deleteAll();
        rentalRepository.deleteAll();

        carRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void setBaseURI(Class<?> testClass, Integer port) {
        String className = testClass.getSimpleName();

        switch (className) {
            case "AuthControllerTest":
                RestAssured.baseURI = "http://localhost:" + port + "/auth";
                break;
            case "CarControllerTest":
                RestAssured.baseURI = "http://localhost:" + port + "/cars";
                break;
            case "RentalControllerTest":
                RestAssured.baseURI = "http://localhost:" + port + "/rentals";
                break;
            case "UserControllerTest":
                RestAssured.baseURI = "http://localhost:" + port + "/user";
                break;
            case "PaymentsControllerTest":
                RestAssured.baseURI = "http://localhost:" + port + "/payments";
                break;
            default:
                break;
        }
    }

}
