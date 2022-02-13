package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.ConverterPostDto;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.PostRepository;
import com.salesianostriana.miarma.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
//    private final ConverterPostDto converterPostDto;

    @Override
    public Post save(CreatePostDto post, UserEntity currentUser) {

        return postRepository.save(Post.builder()
                .file(post.getFile())
                .message(post.getMessage())
                .owner(currentUser)
                .isNotVisible(currentUser.isPrivate())
                .build());
        
    }
}
