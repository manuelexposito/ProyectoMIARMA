package com.salesianostriana.miarma.repositories;


import com.salesianostriana.miarma.models.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {
}
