package com.didenko.gameservice.controller;

import com.didenko.gameservice.service.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* Controller that provides common info
* Genre list, Platform List, catalogues etc...
* */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UtilRestController {

    private final UtilService utilService;

    @CrossOrigin
    @GetMapping("/platforms")
    public ResponseEntity<String> getPlatforms() {
        return new ResponseEntity<>(utilService.getPlatformsList(), HttpStatus.OK);
    }

}
