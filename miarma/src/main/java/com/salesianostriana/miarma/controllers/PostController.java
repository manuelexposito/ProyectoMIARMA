package com.salesianostriana.miarma.controllers;


import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.ConverterPostDto;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.post.dto.PostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.services.PostService;
import com.salesianostriana.miarma.utils.pagination.PaginationLinksUtils;
import io.github.techgnious.exception.VideoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: Ver como conectar la api con flutter
//@CrossOrigin(origins = "http://localhost:58367/")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ConverterPostDto converterPostDto;
    private final PaginationLinksUtils paginationLinksUtils;

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

    @GetMapping("/{id}")
    public PostDto getOnePost(@PathVariable UUID id,@AuthenticationPrincipal UserEntity currentUser){

        Post foundPost = postService.getOnePost(id, currentUser);
        return converterPostDto.convertPostToPostDto(foundPost);

    }

    @GetMapping("/public")
    public ResponseEntity<Page<PostDto>> getPublicPosts(@PageableDefault() Pageable pageable, HttpServletRequest request){

        Page<PostDto> posts = postService.getPublicPosts(pageable)
                .map(converterPostDto::convertPostToPostDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(posts, uriBuilder))
                .body(posts);

    }


    @GetMapping("/all/{username}")
    public ResponseEntity<Page<PostDto>> getUsersPost(@PathVariable String username, @AuthenticationPrincipal UserEntity currentUser, Pageable pageable, HttpServletRequest request){

        Page<PostDto> posts = postService.getPostsByUsername(username, currentUser, pageable)
                .map(converterPostDto::convertPostToPostDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link", paginationLinksUtils.createLinkHeader(posts, uriBuilder)).body(posts);


    }

    @GetMapping("/me")
    public ResponseEntity<Page<PostDto>> getMyPosts(@AuthenticationPrincipal UserEntity currentUser, Pageable pageable, HttpServletRequest request){

        Page<PostDto> posts = postService.getMyPosts(currentUser, pageable).map(converterPostDto::convertPostToPostDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link", paginationLinksUtils.createLinkHeader(posts, uriBuilder)).body(posts);

    }





}
