package com.didenko.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateEditDto {

    @Size(min = 4, max = 20, message = "username must between 4 and 20 characters")
    @NotBlank(message = "username is mandatory")
    private String username;

    @Email
    private String email;

    @Size(min = 6, message = "password must be at least 6 characters long")
    @NotBlank
    private String password;
}