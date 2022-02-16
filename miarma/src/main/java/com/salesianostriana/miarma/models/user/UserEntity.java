package com.salesianostriana.miarma.models.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.user.role.UserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;



///ENTITY GRAPH
@NamedEntityGraph(

        name = "grafo-user-posts",
        attributeNodes = {
                @NamedAttributeNode("posts")
        }

)
///ENTITY GRAPH

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

    @Past
    private LocalDate birthdate;

    private String fullName;

    private boolean isPrivate;

    @Length(max = 250)
    private String biography;

    private String avatar;


    // SOL 1 CREAR UNA NUEVA ENTIDAD --> SEGUIMIENTO
    /*
                Seguimiento tendrá User ORIGEN y User DESTINO (AMBAS @ManyToOne)
                Ahora en User podremos tener dos @OneToMany
     */

    //SOL 2 NO TENER RELACIONES Y GESTIONAR LAS RELACIONES CON CONSULTAS, CONSULTANDO LA ENTIEDAD "SEGUIMIENTO"
        //Para el id de seguidor y seguimiento se podría usar @EmbbededId para evitar la repetición (Mirar los ManyToMany con EXTRA)




    /*
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

*/
    //TODO: Añadir QUERIES en UserEntityRepository para hacer la relación con Followers y Following

    @Builder.Default
    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @Builder.Default
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
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
