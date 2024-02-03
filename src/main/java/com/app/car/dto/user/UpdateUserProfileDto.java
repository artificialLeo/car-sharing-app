package com.app.car.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileDto(
        @Size(min = 3, max = 35)
        String firstName,
        @Size(min = 3, max = 35)
        String lastName,
        @Email
        String email,
        @Size(min = 8, max = 20)
        String password,
        @Size(min = 8, max = 35)
        String repeatPassword
) {

}
