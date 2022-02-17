package com.salesianostriana.miarma.repositories;

import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.follow.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {


    @Query(value = """
            SELECT *
            FROM Follow f
            WHERE f.followed_id = :id1
            """, nativeQuery = true)
    List<Follow> findFollowByRequestedId(@Param("id1") UUID idFollowed);


}
