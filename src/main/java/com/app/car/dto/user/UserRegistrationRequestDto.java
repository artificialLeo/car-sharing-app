package com.app.car.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 35, message = "Password must be between 8 and 35 characters")
    private String password;

    @NotBlank(message = "Repeat password cannot be blank")
    @Size(min = 8, max = 35, message = "Repeat password must be between 8 and 35 characters")
    private String repeatPassword;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, max = 35, message = "First name must be between 3 and 35 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 3, max = 35, message = "Last name must be between 3 and 35 characters")
    private String lastName;
}
