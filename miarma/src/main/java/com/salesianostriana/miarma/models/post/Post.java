package com.salesianostriana.miarma.models.post;

import com.salesianostriana.miarma.models.comment.Comment;
import com.salesianostriana.miarma.models.user.UserEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Post implements Serializable {

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

    private String message;

    private String file;

    @CreatedDate
    private LocalDateTime createdAt;


    @ManyToOne
    private UserEntity owner;

    @Builder.Default
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private boolean isNotVisible;// !owner.isPrivate();

}
