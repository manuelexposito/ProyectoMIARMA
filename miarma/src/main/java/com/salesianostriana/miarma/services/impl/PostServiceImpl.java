package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.ConverterPostDto;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.PostRepository;
import com.salesianostriana.miarma.services.PostService;
import com.salesianostriana.miarma.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;

    @Override
    public Post save(CreatePostDto post, MultipartFile file, UserEntity currentUser) {

        String filename = storageService.store(file);

        String uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();

        Post newPost = Post.builder()
                .file(uri)
                .message(post.getMessage())
                .owner(currentUser)
                .isNotVisible(currentUser.isPrivate())
                .build();

        currentUser.setUserToPost(newPost);

        return postRepository.save(newPost);

    }


}
