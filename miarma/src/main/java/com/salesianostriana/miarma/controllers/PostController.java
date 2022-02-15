package com.salesianostriana.miarma.controllers;


import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.ConverterPostDto;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.post.dto.PostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.services.PostService;
import io.github.techgnious.exception.VideoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ConverterPostDto converterPostDto;

    //TODO: NEW POST (logged)
    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@RequestPart("file")MultipartFile file,
                                              @RequestPart("body")CreatePostDto newPost,
                                              @AuthenticationPrincipal UserEntity currentUser) throws Exception {


        Post savedPost = postService.save(newPost, file, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(converterPostDto.convertPostToPostDto(savedPost));


    }

    //TODO: EDIT POST (logged)
    //TODO: DELETE POST (logged)
    //TODO: GET ALL POSTS
    //TODO: GET ONE POST
    //TODO: GET SOMEONE'S POST
    //TODO: GET MY POSTS (logged)

}
