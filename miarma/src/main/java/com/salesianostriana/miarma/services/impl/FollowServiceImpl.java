package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.follow.FollowPK;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.FollowRepository;
import com.salesianostriana.miarma.services.FollowService;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserEntityService userService;

    @Override
    public Follow sendRequest(UserEntity currentUser, String username){

        Optional<UserEntity> userRequested = userService.findByUsername(username);
        UserEntity foundUser = new UserEntity();

        Follow followRequest = Follow.builder()
                .userFollowing(currentUser)
                .build();


        //Buscamos al usuario, y setea los IDs si lo encuentra
        if (userRequested.isPresent()){
            foundUser = userRequested.get();
            followRequest.setUserFollowed(foundUser);

        } else{
            throw new SingleEntityNotFoundException(UserEntity.class);
        }

        //Buscamos si existe esta relación FOLLOW



        //Si la peticion no existe, la creamos y la dejamos en "pendiente" seteandola en FALSE
        if(!foundUser.getRequests().contains(followRequest)){

            followRequest.setAccepted(false);
            foundUser.getRequests().add(followRequest);
            followRepository.save(followRequest);
            userService.save(foundUser);


        }
        //Dará un error CONFLICT 409 en caso de que la petición existe

        return followRequest;
    }

    @Override
    public Follow save(Follow followRequest, UserEntity currentUser) {

        followRequest = Follow.builder()
                .isAccepted(true)
                .build();

        return followRepository.save(followRequest);
    }


    @Override
    public void delete(FollowPK id) {

    }
}
