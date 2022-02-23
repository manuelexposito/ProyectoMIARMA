package com.salesianostriana.miarma.services;

import com.salesianostriana.miarma.models.comment.Comment;
import com.salesianostriana.miarma.models.comment.dto.CreateCommentDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommentService {

    Page<Comment> getAllComments(UUID postId, Pageable pageable);

    Comment add(UUID postId, CreateCommentDto dto, UserEntity currentUser);

    Comment edit(Long id, CreateCommentDto dto, UserEntity currentUser);

    void delete(Long id, UserEntity currentUser);

}
