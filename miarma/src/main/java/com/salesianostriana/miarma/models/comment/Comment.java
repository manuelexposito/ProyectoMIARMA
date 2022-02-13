package com.salesianostriana.miarma.models;

import com.salesianostriana.miarma.models.user.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    @CreatedDate
    private LocalDateTime createdAt;

    //private Long likes ?


}
