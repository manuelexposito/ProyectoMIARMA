package com.salesianostriana.miarma.controllers;

import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

   // private final UserEntityService userService;
    private final FollowService followService;


    @PostMapping("/{username}")
    public ResponseEntity<Follow> sendFollowRequest(@PathVariable String username, @AuthenticationPrincipal UserEntity currentUser){

        return ResponseEntity.status(HttpStatus.CREATED).body(followService.sendRequest(currentUser, username));

    }



}
