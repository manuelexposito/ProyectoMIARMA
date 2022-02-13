package com.salesianostriana.miarma.models.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String avatar, username, fullName, email, role;
    private LocalDateTime birthdate;

}
