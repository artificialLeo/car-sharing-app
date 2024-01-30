package com.app.car.service;

import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserResponseDto;
import com.app.car.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
