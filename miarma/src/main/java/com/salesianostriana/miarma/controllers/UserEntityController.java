package com.salesianostriana.miarma.controllers;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.models.user.dto.UserDto;
import com.salesianostriana.miarma.models.user.dto.UserDtoConverter;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;


    @PostMapping("/auth/register")
    public ResponseEntity<UserDto> signIn(@Valid @RequestPart("body") CreateUserDto newUser, @RequestPart("file") MultipartFile avatar) throws Exception {

        UserEntity saved = userEntityService.save(newUser, avatar);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.convertUserEntityToGetUserDto(saved));
    }


    @GetMapping("/profile/{id}")
    public UserDto getUserProfile(@PathVariable UUID id, @AuthenticationPrincipal UserEntity currentUser) {

        UserEntity user = userEntityService.getUserProfile(id, currentUser);

        return userDtoConverter.convertUserEntityToGetUserDto(user);

    }


    @PutMapping("/profile/me")
    public UserDto editMyProfile(@Valid @RequestPart("body") CreateUserDto editUser,
                                 @RequestPart("file") MultipartFile file,
                                 @AuthenticationPrincipal UserEntity currentUser) throws Exception {

        UserEntity userEdited =userEntityService.edit(editUser,file,currentUser);

        return userDtoConverter.convertUserEntityToGetUserDto(userEdited);


    }


    //Método exclusivo para una rápida corrección en postman:
    @GetMapping("/allUsers")
    public List<UserDto> getAllUsers (){
        return userEntityService.findAllUsers().stream().map(userDtoConverter::convertUserEntityToGetUserDto).collect(Collectors.toList());
    }

}
