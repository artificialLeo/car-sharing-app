package com.app.car.dto.user;

import com.app.car.model.enums.UserRole;

public record UserProfileDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        UserRole role
) {

}
