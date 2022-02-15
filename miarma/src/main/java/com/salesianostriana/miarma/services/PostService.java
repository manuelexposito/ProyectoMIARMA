package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import io.github.techgnious.exception.VideoException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {

    Post save(CreatePostDto post, MultipartFile file, UserEntity currentUser) throws Exception;



}
