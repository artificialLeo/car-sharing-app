package com.app.car.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileDto(
        @Size(min = 3, max = 35, message = "First name must be between 3 and 35 characters")
        String firstName,
        @Size(min = 3, max = 35, message = "Last name must be between 3 and 35 characters")
        String lastName,
        @Email(message = "Invalid email format")
        String email,
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password,
        @Size(min = 8, max = 35, message = "Repeat password must be between 8 and 35 characters")
        String repeatPassword
) {

}

