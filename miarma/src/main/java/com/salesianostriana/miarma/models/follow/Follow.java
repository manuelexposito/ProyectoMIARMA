package com.salesianostriana.miarma.models.follow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salesianostriana.miarma.models.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Follow implements Serializable {

    @Builder.Default
    @EmbeddedId
    private FollowPK followPK = new FollowPK();

    @ManyToOne
    @MapsId("userFollowed")
    @JoinColumn(name = "followed_id")
    @JsonIgnore
    private UserEntity userFollowed;


    @ManyToOne
    @MapsId("userFollowing")
    @JoinColumn(name = "following_id")
    @JsonIgnore
    private UserEntity userFollowing;

    private boolean isAccepted;



}
