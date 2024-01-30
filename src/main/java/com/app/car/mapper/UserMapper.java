package com.app.car.mapper;

import com.app.car.dto.user.UserResponseDto;
import com.app.car.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserResponseDto toUserResponse(User user);
}
