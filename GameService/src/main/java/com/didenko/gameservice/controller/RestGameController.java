package com.didenko.gameservice.controller;

import com.didenko.gameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proto.Game;

import java.util.List;

@RequestMapping("/game")
@RequiredArgsConstructor
@RestController
public class RestGameController {

    private final GameService gameService;

    @GetMapping("/status/health")
    public ResponseEntity<List<Game>> checkHealth(){
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/tolist")
    public ResponseEntity<HttpStatus> addToList(@RequestBody AddToListParams params){
        gameService.addGameToList(params);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}