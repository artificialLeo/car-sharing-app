package com.app.car.service;

import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.dto.user.UserLoginResponseDto;
import com.app.car.exception.MockException;
import com.app.car.security.AuthenticationService;
import com.app.car.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            throw new MockException("Error initializing mocks : " + e);
        }
    }

    @Test
    @DisplayName("Authenticate success")
    void authenticate_ValidCredentials_ReturnsUserLoginResponseDto() {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtil.generateToken(requestDto.email())).thenReturn("testToken");

        UserLoginResponseDto result = authenticationService.authenticate(requestDto);

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );

        verify(jwtUtil, times(1)).generateToken(requestDto.email());

        assertEquals("testToken", result.token());
    }
}

