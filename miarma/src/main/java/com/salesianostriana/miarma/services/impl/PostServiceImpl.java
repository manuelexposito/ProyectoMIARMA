package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.repositories.PostRepository;
import com.salesianostriana.miarma.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

}
