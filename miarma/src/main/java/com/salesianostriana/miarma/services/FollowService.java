package com.salesianostriana.miarma.services;


import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.follow.FollowPK;
import com.salesianostriana.miarma.models.user.UserEntity;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

public interface FollowService {


    Follow sendRequest(UserEntity currentUser, String username);

    List<Follow> getPetitionsList(UserEntity currentUser);

    Follow save(Follow request, UserEntity currentUser);

    void delete(FollowPK id);

}
