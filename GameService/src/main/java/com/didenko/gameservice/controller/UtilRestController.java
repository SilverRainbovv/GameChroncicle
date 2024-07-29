package com.didenko.gameservice.controller;

import com.didenko.gameservice.dto.GameBestAllTimeDto;
import com.didenko.gameservice.service.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/*
* Controller that provides common info
* Genre list, Platform List, catalogues etc...
* */
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UtilRestController {

    private final UtilService utilService;

    @GetMapping("/platforms")
    public ResponseEntity<String> getPlatforms() {
        String responseBody = utilService.getPlatformsList();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/games/best")
    public ResponseEntity<List<GameBestAllTimeDto>> getBestOfAllTime() {
        List<GameBestAllTimeDto> gameList = utilService.getBestOfAllTime();
        Collections.shuffle(gameList);
        List<GameBestAllTimeDto> shuffledChunk = gameList.stream().limit(24).toList();
        return new ResponseEntity<>(shuffledChunk, HttpStatus.OK);
    }

}