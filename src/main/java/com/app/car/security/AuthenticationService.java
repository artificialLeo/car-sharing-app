package com.app.car.security;

import com.app.car.dto.user.UserLoginRequestDto;
import com.app.car.model.User;
import com.app.car.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.email());

        return user.isPresent() && user.get().getPassword().equals(requestDto.password());
    }
}
