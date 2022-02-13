package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;

public interface PostService {

    Post save(CreatePostDto post, UserEntity currentUser);



}
