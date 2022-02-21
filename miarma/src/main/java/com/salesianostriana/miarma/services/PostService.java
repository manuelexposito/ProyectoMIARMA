package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {

    Post save(CreatePostDto post, MultipartFile file, UserEntity currentUser) throws Exception;

    Page<Post> getPublicPosts(Pageable pageable);

    Page<Post> getPostsByUsername(String username, UserEntity currentUser, Pageable pageable);

    Page<Post> getMyPosts(UserEntity currentUser, Pageable pageable);

    Post getOnePost(UUID id, UserEntity currentUser);

    Post editPost(UUID id, MultipartFile file ,UserEntity currentUser, CreatePostDto editPost) throws Exception;

    void delete(UUID id) throws IOException;

}
