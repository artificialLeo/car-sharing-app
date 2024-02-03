package com.app.car.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 35)
    private String password;
    @NotBlank
    @Size(min = 8, max = 35)
    private String repeatPassword;
    @NotBlank
    @Size(min = 3, max = 35)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 35)
    private String lastName;
}
