package com.app.car.mapper;

import com.app.car.dto.user.UserProfileDto;
import com.app.car.dto.user.UserRegistrationResponseDto;
import com.app.car.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "email", target = "email")
    @Mapping(source = "id", target = "id")
    UserRegistrationResponseDto toUserResponse(User user);

    UserProfileDto toUserProfileDto(User user);
}
