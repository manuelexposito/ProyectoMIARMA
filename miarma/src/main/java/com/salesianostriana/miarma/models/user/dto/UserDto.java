package com.salesianostriana.miarma.models.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String avatar, username, fullname, email, role, biography;
    private boolean isPrivate;

    private LocalDate birthdate;

}
