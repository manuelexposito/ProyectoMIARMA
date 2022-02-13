package com.salesianostriana.miarma.models.user.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String avatar;
    private String username;
    private String fullName;
    private String email;
    private String role;

}
