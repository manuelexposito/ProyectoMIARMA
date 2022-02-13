package com.salesianostriana.miarma.models.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianostriana.miarma.models.comment.dto.CommentDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private String message;

    //TODO: Validaciones para archivo
    private String file;

    private String userFullName;

    private String username;


}
