package com.salesianostriana.miarma;

import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    public final UserEntityService userService;


    @PostConstruct
    public void initData(){
        //TODO: Hacer una imagen default para los que no escogen foto
        String defaultImg = "";
        List<UserEntity> users = List.of(

            UserEntity.builder()
                    .avatar(defaultImg)
                    .birthdate(LocalDate.of(1982, 05, 15))
                    .biography("Expetenda tincidunt in sed, ex partem placerat sea, porro commodo ex eam. His putant aeterno interesset at. Usu ea mundi tincidunt, omnium virtute aliquando ius ex. Ea aperiri sententiae duo.")
                    .email("nere1982@gmail.com")
                    .fullName("Nerea Velazquez Llanos")
                    .isPrivate(false)
                    .password("$2a$10$DnV3dnO9kUqltqxZ8Ef8uOhq93G1NTz/MuM8C6X0SljpIUue8qcSe")
                    .username("nerevelaz1982")
                    .build(),

                UserEntity.builder()
                        .avatar(defaultImg)
                        .birthdate(LocalDate.of(1990, 06, 30))
                        .biography("Expetenda tincidunt in sed, ex partem placerat sea, porro commodo ex eam. His putant aeterno interesset at. Usu ea mundi tincidunt, omnium virtute aliquando ius ex. Ea aperiri sententiae duo.")
                        .email("ismaval@gmail.com")
                        .fullName("Ismael Valdez Bravo")
                        .isPrivate(true)
                        .password("$2a$10$DnV3dnO9kUqltqxZ8Ef8uOhq93G1NTz/MuM8C6X0SljpIUue8qcSe")
                        .username("ismavalxXx")
                        .build(),

                UserEntity.builder()
                        .avatar(defaultImg)
                        .birthdate(LocalDate.of(1974, 04, 07))
                        .biography("Expetenda tincidunt in sed, ex partem placerat sea, porro commodo ex eam. His putant aeterno interesset at. Usu ea mundi tincidunt, omnium virtute aliquando ius ex. Ea aperiri sententiae duo.")
                        .email("dariorive@gmail.com")
                        .fullName("Dario Rivero Miranda")
                        .isPrivate(true)
                        .password("$2a$10$DnV3dnO9kUqltqxZ8Ef8uOhq93G1NTz/MuM8C6X0SljpIUue8qcSe")
                        .username("darive74")
                        .build(),

                UserEntity.builder()
                        .avatar(defaultImg)
                        .birthdate(LocalDate.of(1985, 06, 14))
                        .biography("Expetenda tincidunt in sed, ex partem placerat sea, porro commodo ex eam. His putant aeterno interesset at. Usu ea mundi tincidunt, omnium virtute aliquando ius ex. Ea aperiri sententiae duo.")
                        .email("carlotafariascorrea@gmail.com")
                        .fullName("Carlota Farias Correa")
                        .isPrivate(false)
                        .password("$2a$10$DnV3dnO9kUqltqxZ8Ef8uOhq93G1NTz/MuM8C6X0SljpIUue8qcSe")
                        .username("cafaco85")
                        .build()

        );

        userService.saveAll(users);

    }

}
