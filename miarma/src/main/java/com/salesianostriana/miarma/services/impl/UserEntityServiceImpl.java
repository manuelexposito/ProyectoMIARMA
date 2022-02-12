package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.models.role.UserRole;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.models.user.dto.CreateUserDto;
import com.salesianostriana.miarma.repositories.UserEntityRepository;
import com.salesianostriana.miarma.services.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService, UserDetailsService {

    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.repository.findFirstByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email + " no encontrado"));
    }

    @Override
    public UserEntity save(CreateUserDto newUser) {

        UserEntity userEntity = UserEntity.builder()
                .password(passwordEncoder.encode(newUser.getPassword()))
                .avatar(newUser.getAvatar())
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

}
