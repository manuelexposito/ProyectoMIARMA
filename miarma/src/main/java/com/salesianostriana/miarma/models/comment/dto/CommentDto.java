package com.salesianostriana.miarma.models.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    private String userFullName, username, message;
    private LocalDateTime createdAt;
    private boolean isEdited;
}
