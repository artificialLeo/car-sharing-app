package com.app.car.service;

import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserProfileDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserRegistrationResponseDto;
import com.app.car.exception.RegistrationException;
import com.app.car.model.User;
import com.app.car.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
    void updateUserRole(Long userId, UserRole newRole);
    UserProfileDto getMyProfileInfo();
    UpdateUserProfileDto updateMyProfileInfo(UpdateUserProfileDto updatedUser);
}
