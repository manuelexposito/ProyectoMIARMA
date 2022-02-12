package com.salesianostriana.miarma.repositories;


import com.salesianostriana.miarma.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID> {
}
