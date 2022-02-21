package com.salesianostriana.miarma.models.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.miarma.models.comment.dto.CommentDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private UUID id;
    private String message,file, resizedFile, userFullName, username, userAvatar;


}
