package com.salesianostriana.miarma.controllers;

import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/list")
    public List<Follow> getFollowList(@AuthenticationPrincipal UserEntity currentUser){

        return followService.getPetitionsList(currentUser);

    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptFollowRequest(@PathVariable("id")UUID id, @AuthenticationPrincipal UserEntity currentUser){

        return ResponseEntity.status(HttpStatus.CREATED).body(followService.save(id, currentUser));


    }

    @DeleteMapping("/decline/{id}")
    public ResponseEntity<?> declineFollowRequest(@PathVariable("id")UUID id, @AuthenticationPrincipal UserEntity currentUser){

        followService.delete(id, currentUser);
        return ResponseEntity.noContent().build();

    }


}
