package com.salesianostriana.miarma.models.post.dto;

import com.salesianostriana.miarma.models.post.Post;
import org.springframework.stereotype.Component;

@Component
public class ConverterPostDto {

    public PostDto convertPostToPostDto(Post post){

        return PostDto.builder()
                .id(post.getId())
                .message(post.getMessage())
                .file(post.getFile())
                .resizedFile(post.getResizedFile())
                .username(post.getOwner().getUsername())
                .userFullName(post.getOwner().getFullName())
                .userAvatar(post.getOwner().getAvatar())
                .build();

    }

}
