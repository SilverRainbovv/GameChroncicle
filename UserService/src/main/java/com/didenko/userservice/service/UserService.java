package com.didenko.userservice.service;

import com.didenko.userservice.dto.UserCreateEditDto;
import com.didenko.userservice.dto.UserReadDto;
import com.didenko.userservice.entity.Role;
import com.didenko.userservice.entity.User;
import com.didenko.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User createUser(UserCreateEditDto user) {

        User userEntity = User.builder()
                .UUID(UUID.randomUUID().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .role(Role.USER)
                .build();

        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new RuntimeException("This username is already in use");
        } else if (userRepository.findByEmail(userEntity.getEmail()) != null) {
            throw new RuntimeException("This email is already in use");
        }

        return userRepository.save(userEntity);
    }

    public UserReadDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return UserReadDto.builder()
                .UUID(user.getUUID())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}