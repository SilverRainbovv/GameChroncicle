package com.didenko.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "users")
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String UUID;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String username;

    private String email;

    private String password;
}