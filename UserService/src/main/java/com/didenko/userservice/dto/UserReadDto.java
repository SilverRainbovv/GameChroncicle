package com.didenko.userservice.dto;

import com.didenko.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto {

    private String UUID;
    private String username;
    private String email;
    private String password;
    private Role role;
}
