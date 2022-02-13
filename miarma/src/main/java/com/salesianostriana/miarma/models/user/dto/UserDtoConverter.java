package com.salesianostriana.miarma.models.user.dto;

import com.salesianostriana.miarma.models.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertUserEntityToGetUserDto(UserEntity user) {
        return UserDto.builder()
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();


    }



}
