package com.app.car.controller;

import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserResponseDto;
import com.app.car.exception.RegistrationException;
import com.app.car.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody UserRegistrationRequestDto requestDto) throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public boolean login(@RequestBody UserLoginRequestDto requestDto) {
        try {
            UserDetails userDetails = userService.loadUserByEmailAndPassword(
                    requestDto.email(), requestDto.password());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return false;
        }
    }

}
