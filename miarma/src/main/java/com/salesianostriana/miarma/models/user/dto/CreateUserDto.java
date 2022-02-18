package com.salesianostriana.miarma.models.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianostriana.miarma.validation.annotations.FieldsValueMatch;
import com.salesianostriana.miarma.validation.annotations.StrongPassword;
import lombok.*;

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

    private String avatar;

    @NotEmpty
    @NotNull
    private String fullname;
    @Email
    private String email;

    @Past
    private LocalDate birthdate;

    @Builder.Default
    private boolean isPrivate = false;


    @StrongPassword(min = 8, max = 120, hasUpper = true, hasOther = true, hasNumber = true, hasLower = true, message = "{password.not.strong}")
    private String password;
    private String password2;

}
