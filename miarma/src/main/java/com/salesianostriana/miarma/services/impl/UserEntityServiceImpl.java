package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.errors.exceptions.entitynotfound.EntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.following.PrivateProfileException;
import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.user.dto.UserDto;
import com.salesianostriana.miarma.models.user.role.UserRole;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.repositories.FollowRepository;
import com.salesianostriana.miarma.repositories.UserEntityRepository;
import com.salesianostriana.miarma.services.FollowService;
import com.salesianostriana.miarma.services.ImageScaler;
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
    private final FollowRepository followRepository;
    private final ImageScaler imageScaler;

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

            BufferedImage resized = imageScaler.simpleResizeImage(originalImage, 128);

            OutputStream out = Files.newOutputStream(storageService.load(filename));

            ImageIO.write(resized, ext, out);

            uri = storageService.convertToUri(filename);

        }


        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode(newUser.getPassword()))
                .avatar(uri)
                .birthdate(newUser.getBirthdate())
                .username(newUser.getUsername())
                .fullName(newUser.getFullname())
                .isPrivate(newUser.isPrivate())
                .biography(newUser.getBiography())
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
    public List<UserEntity> saveAll(List<UserEntity> list) {

        return repository.saveAll(list);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return repository.save(userEntity);
    }


    //TODO: Arreglar la privacidad
    @Override
    public UserEntity getUserProfile(UUID id, UserEntity currentUser) {


        Optional<UserEntity> user = repository.findById(id);

        if (user.isPresent()) {

            UserEntity foundUser = user.get();

            //Si la cuenta es pública, podremos verla.
            //Si la cuenta es privada, hay que comprobar si estamos dentro de sus REQUESTS y
            //está ACEPTADA. De no ser así, no se podrá ver.
            Optional<Follow> followReq = followRepository.findFollowByMultipleId(id, currentUser.getId());
            if (!foundUser.isPrivate() ||
                    foundUser.isPrivate() && followReq.isPresent() && followReq.get().isAccepted()) {

                return foundUser;

            } else {
                throw new PrivateProfileException("No puedes ver este perfil porque es privado. Envía una solicitud de seguimiento y, si te acepta, podrás ver sus publicaciones.");
            }


        } else {
            throw new SingleEntityNotFoundException(UserEntity.class);
        }


    }

    @Override
    public UserEntity edit(CreateUserDto edit, MultipartFile avatar, UserEntity currentUser) throws Exception {

        String uri = currentUser.getAvatar();

        if (!avatar.isEmpty()) {

            storageService.deleteFile(currentUser.getAvatar());
            String filename = storageService.store(avatar);
            String ext = StringUtils.getFilenameExtension(filename);


            BufferedImage originalImage = ImageIO.read(avatar.getInputStream());

            BufferedImage resized = imageScaler.simpleResizeImage(originalImage, 128);

            OutputStream out = Files.newOutputStream(storageService.load(filename));

            ImageIO.write(resized, ext, out);

            uri = storageService.convertToUri(filename);

        }


        currentUser = UserEntity.builder()
                .id(currentUser.getId())
                .password(passwordEncoder.encode(edit.getPassword()))
                .avatar(uri)
                .birthdate(edit.getBirthdate())
                .username(edit.getUsername())
                .fullName(edit.getFullname())
                .isPrivate(edit.isPrivate())
                .email(edit.getEmail())
                .role(UserRole.USER_ROLE)
                .build();

        return repository.save(currentUser);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findFirstByUsername(username);
    }

    //Método exclusivo para una rápida corrección en postman:
    @Override
    public List<UserEntity> findAllUsers(){
        return repository.findAll();
    }


}
