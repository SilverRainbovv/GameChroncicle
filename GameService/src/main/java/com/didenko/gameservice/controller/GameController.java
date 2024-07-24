package com.didenko.gameservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RequiredArgsConstructor
@RestController
public class GameController {

    @GetMapping
    public ResponseEntity<HttpStatus> checkHealth(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}