package com.app.car.mapper;

import com.app.car.dto.user.UserResponseDto;
import com.app.car.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserResponseDto toUserResponse(User user);

    @Mapping(source = "email", target = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", expression = "java(mapAuthorities(user))")
    UserDetails toUserDetails(User user);


    default Collection<? extends GrantedAuthority> mapAuthorities(User user) {
        return user.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
    }
}
