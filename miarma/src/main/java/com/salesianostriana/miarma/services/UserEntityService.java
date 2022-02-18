package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.models.user.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserEntityService{

    UserEntity save(CreateUserDto newUser, MultipartFile avatar) throws Exception;

    UserEntity save(UserEntity userEntity);

    UserEntity getUserProfile(UUID id, UserEntity currentUser);

    UserEntity edit(CreateUserDto edit, MultipartFile avatar, UserEntity currentUser) throws Exception;

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> saveAll(List<UserEntity> list);

    Optional<UserEntity> findById(UUID id);

    List<UserEntity> findAllUsers();

}
