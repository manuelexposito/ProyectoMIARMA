package com.salesianostriana.miarma.repositories;


import com.salesianostriana.miarma.models.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {


    @Query(value = """
            SELECT * FROM Post p
            JOIN User_Entity u on (u.id = p.user_id)
            WHERE p.user_id = :id
            """, nativeQuery = true)
    Page<Post> findAllPostByOwner(@Param("id") UUID id, Pageable pageable);



}
