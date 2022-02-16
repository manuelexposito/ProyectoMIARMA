package com.salesianostriana.miarma.models.follow;

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
    private UserEntity userFollowed;

    @ManyToOne
    @MapsId("userFollowing")
    @JoinColumn(name = "following_id")
    private UserEntity userFollowing;

    private RequestStatus requestStatus;



}
