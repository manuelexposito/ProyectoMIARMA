package com.salesianostriana.miarma.controllers;


import com.salesianostriana.miarma.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //TODO: NEW POST (logged)
    //TODO: EDIT POST (logged)
    //TODO: DELETE POST (logged)
    //TODO: GET ALL POSTS
    //TODO: GET ONE POST
    //TODO: GET SOMEONE'S POST
    //TODO: GET MY POSTS (logged)

}
