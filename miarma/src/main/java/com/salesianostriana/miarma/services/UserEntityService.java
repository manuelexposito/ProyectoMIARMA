package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;


public interface UserEntityService{

    UserEntity save(CreateUserDto newUser, MultipartFile avatar);

    Optional<UserEntity> findById(UUID id);

}
