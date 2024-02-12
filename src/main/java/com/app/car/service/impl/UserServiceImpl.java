package com.app.car.service.impl;

import static com.app.car.exception.user.UserRegistrationException.ExceptionType.EMAIL_ALREADY_EXISTS;
import static com.app.car.exception.user.UserRegistrationException.ExceptionType.PASSWORDS_DO_NOT_MATCH;

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
import com.app.car.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto
    ) throws UserRegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UserRegistrationException(EMAIL_ALREADY_EXISTS);
        }

        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new UserRegistrationException(PASSWORDS_DO_NOT_MATCH);
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setRole(UserRole.ROLE_CUSTOMER);

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public void updateUserRole(Long userId, UserRole newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    public UserProfileDto getMyProfileInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository
                .findByEmail(userEmail)
                .orElseThrow(()
                        -> new UserNotFoundException(userEmail));

        return userMapper.toUserProfileDto(currentUser);
    }

    @Override
    public UpdateUserProfileDto updateMyProfileInfo(UpdateUserProfileDto updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository
                .findByEmail(userEmail)
                .orElseThrow(()
                        -> new UserNotFoundException(userEmail));

        if (StringUtils.hasText(updatedUser.firstName())) {
            currentUser.setFirstName(updatedUser.firstName());
        }

        if (StringUtils.hasText(updatedUser.lastName())) {
            currentUser.setLastName(updatedUser.lastName());
        }

        if (StringUtils.hasText(updatedUser.email())) {
            currentUser.setEmail(updatedUser.email());
        }

        if (
                StringUtils.hasText(updatedUser.password())
                && StringUtils.hasText(updatedUser.repeatPassword())
                && updatedUser.password().equals(updatedUser.repeatPassword())
        ) {
            currentUser.setPassword(passwordEncoder.encode(updatedUser.password()));
        }

        userRepository.save(currentUser);

        return updatedUser;
    }

}
