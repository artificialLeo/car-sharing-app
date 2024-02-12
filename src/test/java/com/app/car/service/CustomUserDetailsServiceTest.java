package com.app.car.service;

import com.app.car.model.User;
import com.app.car.repository.UserRepository;
import com.app.car.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Load User By Email success")
    void loadUserByUsername_Success() {
        String userEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        mockUser.setPassword("hashedPassword");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        assertEquals(userEmail, userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());

        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    @DisplayName("Load User By Email fail")
    void loadUserByUsername_UserNotFound() {
        String userEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        try {
            customUserDetailsService.loadUserByUsername(userEmail);
        } catch (UsernameNotFoundException e) {
            assertEquals("Can't find User with email : " + userEmail, e.getMessage());
        }

        verify(userRepository, times(1)).findByEmail(userEmail);
    }
}
