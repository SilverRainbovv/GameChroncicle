package com.didenko.gameservice.controller;

import com.api.igdb.exceptions.RequestException;
import com.didenko.gameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.Game;

import java.util.List;

@RequestMapping("/game")
@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;

    @GetMapping("/status/health")
    public ResponseEntity<List<Game>> checkHealth(){
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping
    public ResponseEntity<String> getGamesByFilter(){
        return new ResponseEntity<>(gameService.getGamesByFilter(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getGame(@PathVariable Long id) throws RequestException {
        String response = gameService.getGames(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}