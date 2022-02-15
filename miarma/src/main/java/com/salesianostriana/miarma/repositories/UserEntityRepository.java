package com.salesianostriana.miarma.repositories;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.user.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {


    @EntityGraph(value = "grafo-user-posts")
    List<UserEntity> findByIdNotNull();

    Optional<UserEntity> findFirstByEmail(String email);

}
