package com.salesianostriana.miarma.repositories;


import com.salesianostriana.miarma.models.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {


    @Query(value = """
            SELECT * FROM Post p
            JOIN UserEntity u on (u.id = p.owner.id)
            """, nativeQuery = true)
    List<Post> findAllPostByOwnerId(@Param("id") UUID id);


}
