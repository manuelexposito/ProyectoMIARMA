package com.salesianostriana.miarma.models.comment.dto;

import com.salesianostriana.miarma.models.comment.Comment;
import org.springframework.stereotype.Component;

@Component
public class ConverterCommentDto {


    public CommentDto convertCommentToCommentDto(Comment comment){

        return CommentDto.builder()
                .id(comment.getId())
                .username(comment.getOwner().getUsername())
                .userFullName(comment.getOwner().getFullName())
                .message(comment.getMessage())
                .build();

    }


}
