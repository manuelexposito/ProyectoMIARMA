package com.salesianostriana.miarma.models.user;

import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.user.role.UserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails, Serializable {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String username;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String email;

    private String password;

    private String fullName;

    private boolean isPrivate;

    private String avatar;

    //Usuarios que le siguen
    @Builder.Default
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"),
            name = "user_followers")
    private List<UserEntity> followers = new ArrayList<>();

    //Usuarios que sigue
    @Builder.Default
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"),
            name = "user_following")
    private List<UserEntity> following = new ArrayList<>();


    @Builder.Default
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }


    //HELPERS

    public void setUserToPost(Post post){

        this.getPosts().add(post);
        post.setOwner(this);
    }

    public void removeUserFromPost(Post post){

        this.getPosts().remove(post);
        post.setOwner(null);

    }

}
