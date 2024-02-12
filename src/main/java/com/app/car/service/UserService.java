package com.app.car.service;

import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserProfileDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserRegistrationResponseDto;
import com.app.car.exception.user.UserRegistrationException;
import com.app.car.model.enums.UserRole;

public interface UserService {
    UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto
    ) throws UserRegistrationException;

    void updateUserRole(Long userId, UserRole newRole);

    UserProfileDto getMyProfileInfo();

    UpdateUserProfileDto updateMyProfileInfo(UpdateUserProfileDto updatedUser);
}
