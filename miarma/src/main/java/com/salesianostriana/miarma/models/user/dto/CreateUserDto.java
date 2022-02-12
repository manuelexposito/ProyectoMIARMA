package com.salesianostriana.miarma.models.user.dto;


import com.salesianostriana.miarma.validation.annotations.FieldsValueMatch;
import com.salesianostriana.miarma.validation.annotations.StrongPassword;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
//TODO: Mensaje ERRORS para las validaciones
@FieldsValueMatch(field = "password", fieldMatch = "password2", message = "{fieldsvalue.notmatch}" )
public class CreateUserDto {

    @NotEmpty
    @NotNull
    private String username;
    //TODO: Hacer la subida de avatar con fichero
    private String avatar;

    @NotEmpty
    @NotNull
    private String fullname;
    @Email
    private String email;

    @Builder.Default
    private boolean isPrivate = false;


    @StrongPassword(min = 4, max = 10, hasUpper = true, hasOther = true, hasNumber = true, hasLower = true)
    private String password;
    private String password2;

}
