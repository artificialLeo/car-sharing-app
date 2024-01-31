package com.app.car.service;

import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserResponseDto;
import com.app.car.exception.RegistrationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserDetails loadUserByEmailAndPassword(String email, String password) throws UsernameNotFoundException;
}
