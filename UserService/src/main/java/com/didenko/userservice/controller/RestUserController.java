package com.didenko.userservice.controller;

import com.didenko.userservice.dto.UserCreateEditDto;
import com.didenko.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class RestUserController {

    private final UserService userService;

    @GetMapping("/status")
    public String status() {
        return HttpStatus.OK.toString();
    }

    @GetMapping("/wrong_creds")
    public String wrongCreds() {
        return HttpStatus.BAD_REQUEST.toString();
    }

    @GetMapping("/hello")
    public String helloUser(Principal principal) {
        return "Hello " + principal.getName();
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateEditDto userDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.createUser(userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}