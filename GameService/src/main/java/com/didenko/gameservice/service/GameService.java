package com.didenko.gameservice.service;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.JsonRequestKt;
import com.api.igdb.utils.Endpoints;
import com.didenko.gameservice.entity.Game;
import com.didenko.gameservice.entity.GameList;
import com.didenko.gameservice.entity.GameListType;
import com.didenko.gameservice.repository.GameRepository;
import com.didenko.gameservice.util.IgdbWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final IgdbWrapperUtil igdbWrapperUtil;
    private IGDBWrapper igdbWrapper;

    public String getGamesByFilter(){
        if (igdbWrapper == null) {
            igdbWrapper = igdbWrapperUtil.getIGDBWrapper();
        }

        APICalypse apiCalypse = new APICalypse()
                .fields("name, release_dates;")
                .where("release_dates.date > 1721822294 & rating >= 80;");

        try {
            return JsonRequestKt.jsonGames(igdbWrapper, apiCalypse);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "game", key = "#id")
    public String getGames(Long id) {
        if (igdbWrapper == null) {
            igdbWrapper = igdbWrapperUtil.getIGDBWrapper();
        }

        String response;
        String fields = "fields *; search \"Okami\";";

        try {
            System.out.println("call to API");
            response = igdbWrapper.apiJsonRequest(Endpoints.GAMES, fields);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @Transactional(readOnly = false)
    public void addGameToList(Long igdbId, GameListType gameListType) {

        Game game = Game.builder()
                .igdbId(igdbId).build();

        GameList gameList = GameList.builder()
                .userId(1L)
                .UID(UUID.randomUUID().toString())
                .name("name")
                .type(gameListType)
                .build();

        gameList.addGame(game);

        gameRepository.save(gameList);
    }
}