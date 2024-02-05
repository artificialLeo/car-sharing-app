package com.app.car.controller;

import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.dto.user.UserLoginResponseDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserRegistrationResponseDto;
import com.app.car.exception.RegistrationException;
import com.app.car.security.AuthenticationService;
import com.app.car.service.TelegramNotificationService;
import com.app.car.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserRegistrationResponseDto registration(@RequestBody UserRegistrationRequestDto requestDto) throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) throws TelegramApiException {
        return authenticationService.authenticate(requestDto);
    }

}
