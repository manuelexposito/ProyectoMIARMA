package com.salesianostriana.miarma.models.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private String userFullName;

    private String username;

    private String message;
}
