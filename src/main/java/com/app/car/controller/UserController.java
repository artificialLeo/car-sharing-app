package com.app.car.controller;

import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserProfileDto;
import com.app.car.model.enums.UserRole;
import com.app.car.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Update user role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestParam UserRole newRole
    ) {
        userService.updateUserRole(id, newRole);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Get my profile information")
    public ResponseEntity<UserProfileDto> getMyProfileInfo() {
        UserProfileDto myProfile = userService.getMyProfileInfo();
        return ResponseEntity.ok(myProfile);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "Update my profile information")
    public ResponseEntity<UpdateUserProfileDto> updateMyProfileInfo(
            @Valid @RequestBody UpdateUserProfileDto updatedUser
    ) {
        UpdateUserProfileDto updatedUserProfile = userService
                .updateMyProfileInfo(updatedUser);
        return ResponseEntity.ok(updatedUserProfile);
    }
}

