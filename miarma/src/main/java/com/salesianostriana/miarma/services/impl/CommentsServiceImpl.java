package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.errors.exceptions.entitynotfound.EntityNotFoundException;
import com.salesianostriana.miarma.models.comment.Comment;
import com.salesianostriana.miarma.models.comment.dto.CreateCommentDto;
import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.CommentRepository;
import com.salesianostriana.miarma.repositories.PostRepository;
import com.salesianostriana.miarma.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Page<Comment> getAllComments(UUID postId, Pageable pageable) {
        return null;
    }

    @Override
    public Comment add(UUID postId, CreateCommentDto dto, UserEntity currentUser) {

        Optional<Post> post = postRepository.findById(postId);


        if(post.isPresent()){

            Comment newComment = Comment.builder()
                    .createdAt(LocalDateTime.now())
                    .message(dto.getMessage())
                    .owner(currentUser)
                    .post(post.get())
                    .build();
            return commentRepository.save(newComment);
        } else{
            throw new EntityNotFoundException("No ha podido encontrarse esa publicación.");
        }

    }

    @Override
    public Comment edit(Long id, CreateCommentDto dto, UserEntity currentUser) {
        return null;
    }

    @Override
    public void delete(Long id, UserEntity currentUser) {

    }
}
