package com.didenko.userservice.service;

import com.didenko.userservice.dto.UserCreateEditDto;
import com.didenko.userservice.entity.Role;
import com.didenko.userservice.entity.User;
import com.didenko.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public User createUser(UserCreateEditDto user) {

        User userEntity = modelMapper.map(user, User.class);
        userEntity.setRole(Role.USER);
        userEntity.setUUID(UUID.randomUUID().toString());

        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new RuntimeException("This username is already in use");
        } else if (userRepository.findByEmail(userEntity.getEmail()) != null) {
            throw new RuntimeException("This email is already in use");
        }

        return userRepository.save(userEntity);
    }

}