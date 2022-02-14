package com.salesianostriana.miarma.controllers;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.models.user.dto.UserDto;
import com.salesianostriana.miarma.models.user.dto.UserDtoConverter;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;


    @PostMapping("/auth/register")
    public UserDto signIn(@Valid @RequestPart("body") CreateUserDto newUser, @RequestPart("file")MultipartFile avatar) throws Exception {

        UserEntity saved = userEntityService.save(newUser, avatar);

        return userDtoConverter.convertUserEntityToGetUserDto(saved);
    }

    //TODO: GET ONE USER PROFILE
    //TODO: EDIT MY PROFILE
    //TODO: SEND FOLLOW PETITION
    //TODO: ACCEPT FOLLOW
    //TODO: DECLINE FOLLOW

}
