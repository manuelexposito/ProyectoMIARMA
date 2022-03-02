package com.salesianostriana.miarma.models.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianostriana.miarma.validation.annotations.FieldsValueMatch;
import com.salesianostriana.miarma.validation.annotations.StrongPassword;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@FieldsValueMatch(field = "password", fieldMatch = "password2", message = "{fieldsvalue.not.match}" )
public class CreateUserDto {

    @NotEmpty
    @NotNull
    private String username;

    //TODO: Una imagen default para los usuarios que no escogen imagen
    @Builder.Default
    private String avatar = "";

    @NotEmpty
    @NotNull
    private String fullName;
    @Email
    private String email;

    @Past
    private LocalDate birthdate;

    @Lob
    private String biography;

    @Builder.Default
    private boolean isPrivate = false;


    @StrongPassword(min = 8, max = 120, hasUpper = true, hasOther = true, hasNumber = true, hasLower = true, message = "{password.not.strong}")
    private String password;
    private String password2;

}
