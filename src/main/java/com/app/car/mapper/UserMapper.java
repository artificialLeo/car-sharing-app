package com.app.car.mapper;

import com.app.car.dto.user.UserProfileDto;
import com.app.car.dto.user.UserRegistrationResponseDto;
import com.app.car.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "id", target = "id")
    UserRegistrationResponseDto toUserResponse(User user);

    UserProfileDto toUserProfileDto(User user);
}
