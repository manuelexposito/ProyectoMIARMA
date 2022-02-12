package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;

import java.util.Optional;
import java.util.UUID;


public interface UserEntityService{

    UserEntity save(CreateUserDto newUser);

    Optional<UserEntity> findById(UUID id);

}
