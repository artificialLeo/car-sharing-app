package com.app.car.service.impl;

import com.app.car.dto.user.UserRegistrationRequestDto;
import com.app.car.dto.user.UserResponseDto;
import com.app.car.exception.RegistrationException;
import com.app.car.mapper.UserMapper;
import com.app.car.model.User;
import com.app.car.repository.UserRepository;
import com.app.car.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user with email "
                    + requestDto.getEmail());
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserDetails loadUserByEmailAndPassword(String email, String password) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return userMapper.toUserDetails(user);
    }
}
