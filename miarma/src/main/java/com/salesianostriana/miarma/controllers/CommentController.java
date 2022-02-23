package com.salesianostriana.miarma.controllers;

import com.salesianostriana.miarma.models.comment.dto.CommentDto;
import com.salesianostriana.miarma.models.comment.dto.ConverterCommentDto;
import com.salesianostriana.miarma.models.comment.dto.CreateCommentDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ConverterCommentDto converterCommentDto;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID postId, @RequestBody CreateCommentDto dto, @AuthenticationPrincipal UserEntity currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                converterCommentDto.convertCommentToCommentDto(commentService.add(postId, dto, currentUser))
        );


    }

}
