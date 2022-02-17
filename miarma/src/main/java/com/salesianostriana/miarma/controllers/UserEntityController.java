package com.salesianostriana.miarma.controllers;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.models.user.dto.UserDto;
import com.salesianostriana.miarma.models.user.dto.UserDtoConverter;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;


    @PostMapping("/auth/register")
    public UserDto signIn(@Valid @RequestPart("body") CreateUserDto newUser, @RequestPart("file") MultipartFile avatar) throws Exception {

        UserEntity saved = userEntityService.save(newUser, avatar);

        return userDtoConverter.convertUserEntityToGetUserDto(saved);
    }


    @GetMapping("/profile/{id}")
    public UserDto getUserProfile(@PathVariable UUID id, @AuthenticationPrincipal UserEntity currentUser) {

        UserEntity user = userEntityService.getUserProfile(id, currentUser);

        return userDtoConverter.convertUserEntityToGetUserDto(user);

    }

    //TODO: EDIT MY PROFILE
    @PutMapping("/profile/me")
    public UserDto editMyProfile(@Valid @RequestPart("body") CreateUserDto editUser,
                                 @RequestPart("file") MultipartFile file,
                                 @AuthenticationPrincipal UserEntity currentUser) throws Exception {

        UserEntity userEdited =userEntityService.edit(editUser,file,currentUser);

        return userDtoConverter.convertUserEntityToGetUserDto(userEdited);


    }
}
