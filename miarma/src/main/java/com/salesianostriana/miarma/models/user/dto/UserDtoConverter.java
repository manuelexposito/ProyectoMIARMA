package com.salesianostriana.miarma.models.user.dto;

import com.salesianostriana.miarma.models.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convertUserEntityToGetUserDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .isPrivate(user.isPrivate())
                .birthdate(user.getBirthdate())
                .bio(user.getBiography())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();


    }



}
