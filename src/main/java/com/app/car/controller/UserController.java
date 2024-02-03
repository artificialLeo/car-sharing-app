package com.app.car.controller;

import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserProfileDto;
import com.app.car.mapper.UserMapper;
import com.app.car.model.enums.UserRole;
import com.app.car.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
//    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestParam UserRole newRole
    ) {
        userService.updateUserRole(id, newRole);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<UserProfileDto> getMyProfileInfo() {
        UserProfileDto myProfile = userService.getMyProfileInfo();
        return ResponseEntity.ok(myProfile);
    }

    @PutMapping("/me")
//    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<UpdateUserProfileDto> updateMyProfileInfo(
            @Valid @RequestBody UpdateUserProfileDto updatedUser
    ) {
        UpdateUserProfileDto updatedUserProfile = userService
                .updateMyProfileInfo(updatedUser);
        return ResponseEntity.ok(updatedUserProfile);
    }
}
