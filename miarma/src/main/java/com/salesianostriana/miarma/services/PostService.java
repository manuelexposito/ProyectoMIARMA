package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {

    Post save(CreatePostDto post, MultipartFile file, UserEntity currentUser) throws Exception;

    List<Post> getPublicPosts();

    List<Post> getPostsByUsername(String username, UserEntity currentUser);

    List<Post> getMyPosts(UserEntity currentUser);

    Post getOnePost(UUID id, UserEntity currentUser);

    void delete(UUID id) throws IOException;

}
