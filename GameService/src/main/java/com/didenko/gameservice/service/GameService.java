package com.didenko.gameservice.service;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.JsonRequestKt;
import com.api.igdb.utils.Endpoints;
import com.didenko.gameservice.controller.AddToListParams;
import com.didenko.gameservice.entity.Game;
import com.didenko.gameservice.entity.GameList;
import com.didenko.gameservice.entity.GameListType;
import com.didenko.gameservice.repository.GameListRepository;
import com.didenko.gameservice.repository.GameRepository;
import com.didenko.gameservice.util.IgdbWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class GameService {

    private final GameListRepository gameListRepository;
    private final IgdbWrapperUtil igdbWrapperUtil;
    private final GameRepository gameRepository;
    private IGDBWrapper igdbWrapper;

    public String getGamesByFilter() {
        if (igdbWrapper == null) {
            igdbWrapper = igdbWrapperUtil.getIGDBWrapper();
        }

        String dateNow = Instant.now().toString();
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("release_dates.date > ")
                .append(dateNow)
                .append(" & rating >= 80;");
        APICalypse apiCalypse = new APICalypse()
                .fields("name, release_dates;")
                .where(requestBuilder.toString());

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
    public void addGameToList(AddToListParams params) {

        Optional<Game> maybeGame = gameRepository.findByIgdbId(params.igdbId());
        Game game = maybeGame.orElseGet(() ->
                Game.builder()
                        .igdbId(params.igdbId())
                        .build());

        GameList gameList;

        if (params.listType().equals(GameListType.WISHLIST) || params.listType().equals(GameListType.COLLECTION)) {
            Optional<GameList> maybeGameList = gameListRepository.findByUserIdAndType(params.userId(), params.listType());
            gameList = maybeGameList.orElse(
                    GameList.builder()
                            .name(params.listType().getName())
                            .userId(params.userId())
                            .UID(UUID.randomUUID().toString())
                            .type(params.listType())
                            .build());
        } else {
            Optional<GameList> maybeGameList = gameListRepository.findByUserIdAndName(params.userId(), params.listName());
            gameList = maybeGameList.orElse(
                    GameList.builder()
                            .name(params.listName())
                            .userId(params.userId())
                            .UID(UUID.randomUUID().toString())
                            .type(params.listType())
                            .build()
            );
        }
        gameList.addGame(game);

        gameListRepository.save(gameList);
    }
}