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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ConverterPostDto converterPostDto;


    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@RequestPart("file")MultipartFile file,
                                              @RequestPart("body")CreatePostDto newPost,
                                              @AuthenticationPrincipal UserEntity currentUser) throws Exception {


        Post savedPost = postService.save(newPost, file, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(converterPostDto.convertPostToPostDto(savedPost));


    }

    //TODO: EDIT POST (logged)
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> editPost(@PathVariable UUID id,
                                            @RequestPart("file") MultipartFile file,
                                            @RequestPart("body") CreatePostDto editPost,
                                            @AuthenticationPrincipal UserEntity currentUser) throws Exception {

        Post postEdited = postService.editPost(id, file, currentUser, editPost);

        return ResponseEntity.ok().body(converterPostDto.convertPostToPostDto(postEdited));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) throws IOException {

        postService.delete(id);

        return ResponseEntity.noContent().build();


    }


    @GetMapping("/public")
    public List<PostDto> getPublicPosts(){

        return postService.getPublicPosts()
                .stream()
                .map(converterPostDto::convertPostToPostDto)
                .collect(Collectors.toList());

    }

    @GetMapping("/{id}")
    public PostDto getOnePost(@PathVariable UUID id,@AuthenticationPrincipal UserEntity currentUser){

        Post foundPost = postService.getOnePost(id, currentUser);
        return converterPostDto.convertPostToPostDto(foundPost);

    }
    @GetMapping("/all/{username}")
    public List<PostDto> getUsersPost(@PathVariable String username, @AuthenticationPrincipal UserEntity currentUser){

        return postService.getPostsByUsername(username, currentUser)
                .stream().map(converterPostDto::convertPostToPostDto)
                .collect(Collectors.toList());


    }

    @GetMapping("/me")
    public List<PostDto> getMyPosts(@AuthenticationPrincipal UserEntity currentUser){

        return postService.getMyPosts(currentUser).stream().map(converterPostDto::convertPostToPostDto).collect(Collectors.toList());

    }

}
