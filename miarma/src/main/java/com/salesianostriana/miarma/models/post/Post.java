package com.salesianostriana.miarma.models;

import com.salesianostriana.miarma.models.user.UserEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {

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


    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;

    @Builder.Default
    private boolean isVisible = !owner.isPrivate();

}
