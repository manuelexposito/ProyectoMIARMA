package com.salesianostriana.miarma.repositories;


import com.salesianostriana.miarma.models.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {

    @EntityGraph(value = "grafo-posts-user")
    List<Post> findByIdNotNull();

}
