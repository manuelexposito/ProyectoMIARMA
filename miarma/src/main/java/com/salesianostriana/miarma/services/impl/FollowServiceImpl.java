package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.errors.exceptions.entitynotfound.EntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.entitynotfound.ListEntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.following.FollowingSelfException;
import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.follow.FollowPK;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.FollowRepository;
import com.salesianostriana.miarma.services.FollowService;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserEntityService userService;

    @Override
    public Follow sendRequest(UserEntity currentUser, String username) {

        Optional<UserEntity> userRequested = userService.findByUsername(username);
        UserEntity foundUser = new UserEntity();

        Follow followRequest = Follow.builder()
                .userFollowing(currentUser)
                .build();


        //Buscamos al usuario, y setea los IDs si lo encuentra
        if (userRequested.isPresent()) {
            foundUser = userRequested.get();
            followRequest.setUserFollowed(foundUser);
            //TODO: Gestionar que un usuario no pueda seguirse a sí mismo

        } else {
            throw new SingleEntityNotFoundException(UserEntity.class);
        }


        //Buscamos si existe esta relación FOLLOW


        //Si la peticion no existe, la creamos y la dejamos en "pendiente" seteandola en FALSE
        if (!foundUser.getRequests().contains(followRequest)) {

            followRequest.setAccepted(false);
            foundUser.getRequests().add(followRequest);
            followRepository.save(followRequest);
            userService.save(foundUser);


        }
        //Dará un error CONFLICT 409 en caso de que la petición existe

        return followRequest;
    }

    @Override
    public Follow save(UUID followerId, UserEntity currentUser) {

        Optional<Follow> follower = followRepository.findFollowByMultipleId(currentUser.getId(), followerId);
        Follow followRequest;

        if(follower.isPresent() ){
            followRequest = follower.get();
            followRequest.setAccepted(true);
            return followRepository.save(followRequest);

        } else{
            throw new EntityNotFoundException("No pudo encontrase ninguna petición con ese usuario.");
        }

    }


    @Override
    public void delete(UUID followerId, UserEntity currentUser) {

        Optional<Follow> follower = followRepository.findFollowByMultipleId(currentUser.getId(), followerId);
        Follow followRequest;

        if(follower.isPresent() ){
            //Eliminamos la petición de los "request" del usuario y la borramos de la base de datos.
            followRequest = follower.get();
            currentUser.getRequests().remove(followRequest);
            userService.save(currentUser);
            followRepository.delete(followRequest);

        } else{
            throw new EntityNotFoundException("No pudo encontrase ninguna petición con ese usuario.");
        }
    }

    @Override
    public List<Follow> getPetitionsList(UserEntity currentUser) {

        //Buscamos todas las peticiones que no hayan sido aceptadas todavía

        return followRepository.findRequestsNotAccepted(currentUser.getId());

        /*return currentUser.getRequests().stream()
                .filter(request -> !request.isAccepted())
                .collect(Collectors.toList());*/

    }

}

