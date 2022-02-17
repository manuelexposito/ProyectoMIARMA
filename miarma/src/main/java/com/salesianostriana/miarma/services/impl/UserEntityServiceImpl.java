package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.models.user.role.UserRole;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.repositories.UserEntityRepository;
import com.salesianostriana.miarma.services.StorageService;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService, UserDetailsService {

    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.repository.findFirstByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " no encontrado"));
    }

    @Override
    public UserEntity save(CreateUserDto newUser, MultipartFile avatar) throws Exception {

        String uri = null;

        if (!avatar.isEmpty()) {

            String filename = storageService.store(avatar);
            String ext = StringUtils.getFilenameExtension(filename);


            BufferedImage originalImage = ImageIO.read(avatar.getInputStream());

            BufferedImage resized = storageService.simpleResizeImage(originalImage, 128);

            OutputStream out = Files.newOutputStream(storageService.load(filename));

            ImageIO.write(resized, ext, out);

            uri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/download/")
                    .path(filename)
                    .toUriString();

        }


        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode(newUser.getPassword()))
                .avatar(uri)
                .birthdate(newUser.getBirthdate())
                .username(newUser.getUsername())
                .fullName(newUser.getFullname())
                .isPrivate(newUser.isPrivate())
                .email(newUser.getEmail())
                .role(UserRole.USER_ROLE)
                .build();

        return repository.save(userEntity);

    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<UserEntity> saveAll(List<UserEntity> list){

        return repository.saveAll(list);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return repository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findFirstByUsername(username);
    }
}
