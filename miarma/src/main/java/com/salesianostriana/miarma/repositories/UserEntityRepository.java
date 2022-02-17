package com.salesianostriana.miarma.repositories;

import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.user.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {




    Optional<UserEntity> findFirstByUsername(String username);

    Optional<UserEntity> findFirstByEmail(String email);

}
