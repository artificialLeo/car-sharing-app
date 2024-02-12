package com.app.car.service;

import com.app.car.dto.user.UpdateUserProfileDto;
import com.app.car.dto.user.UserProfileDto;
import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserRegistrationResponseDto;
import com.app.car.exception.user.UserRegistrationException;
import com.app.car.exception.user.UserNotFoundException;
import com.app.car.mapper.UserMapper;
import com.app.car.model.User;
import com.app.car.model.enums.UserRole;
import com.app.car.repository.UserRepository;
import com.app.car.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequestDto registrationRequestDto;
    private UserProfileDto userProfileDto;
    private UpdateUserProfileDto updateUserProfileDto;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        registrationRequestDto = UserRegistrationRequestDto.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .repeatPassword("password")
                .build();

        userProfileDto = new UserProfileDto(
                1L,
                "email@example.com",
                "John",
                "Doe",
                UserRole.ROLE_CUSTOMER
        );

        updateUserProfileDto = new UpdateUserProfileDto(
                "John",
                "Doe",
                "email@example.com",
                "password",
                "password"
        );
    }

    @Test
    @DisplayName("register success")
    void register_ValidRegistrationRequest_Success() throws UserRegistrationException {
        when(userRepository.findByEmail(registrationRequestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationRequestDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.toUserResponse(any(User.class))).thenReturn(new UserRegistrationResponseDto());

        UserRegistrationResponseDto result = userService.register(registrationRequestDto);

        assertNotNull(result);
    }

    @Test
    @DisplayName("register -> RegistrationException")
    void register_UserWithEmailAlreadyExists_ExceptionThrown() {
        when(userRepository.findByEmail(registrationRequestDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserRegistrationException.class, () -> userService.register(registrationRequestDto));
    }

    @Test
    @DisplayName("updateUserRole success")
    void updateUserRole_ValidUserIdAndNewRole_Success() {
        Long userId = 1L;
        UserRole newRole = UserRole.ROLE_MANAGER;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.updateUserRole(userId, newRole);

        assertEquals(newRole, user.getRole());
    }

    @Test
    @DisplayName("updateUserRole -> UserNotFoundException")
    void updateUserRole_InvalidUserId_ExceptionThrown() {
        Long userId = 1L;
        UserRole newRole = UserRole.ROLE_MANAGER;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserRole(userId, newRole));
    }

    @Test
    @DisplayName("getMyProfileInfo success")
    void getMyProfileInfo_ValidAuthentication_ReturnsUserProfileDto() {
        String userEmail = "test@example.com";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUser = new User();
        currentUser.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(currentUser));
        when(userMapper.toUserProfileDto(currentUser)).thenReturn(userProfileDto);

        UserProfileDto result = userService.getMyProfileInfo();

        assertNotNull(result);
    }

    @Test
    @DisplayName("getMyProfileInfo -> NullPointerException")
    void getMyProfileInfo_InvalidAuthentication_ExceptionThrown() {
        SecurityContextHolder.getContext().setAuthentication(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> userService.getMyProfileInfo());
    }


    @Test
    @DisplayName("updateMyProfileInfo success")
    void updateMyProfileInfo_ValidUpdateUserProfileDto_Success() {
        String userEmail = "test@example.com";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User currentUser = new User();
        currentUser.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(currentUser));

        UpdateUserProfileDto result = userService.updateMyProfileInfo(updateUserProfileDto);

        assertNotNull(result);
    }

    @Test
    @DisplayName("updateMyProfileInfo -> NullPointerException")
    void updateMyProfileInfo_InvalidAuthentication_ExceptionThrown() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertThrows(NullPointerException.class, () -> userService.updateMyProfileInfo(updateUserProfileDto));
    }

}
