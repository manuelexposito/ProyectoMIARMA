package com.salesianostriana.miarma.repositories;

import com.salesianostriana.miarma.models.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
