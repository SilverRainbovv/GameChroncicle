package com.didenko.gameservice.service;

import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.utils.Endpoints;
import com.didenko.gameservice.dto.ImageSize;
import com.didenko.gameservice.dto.GameBestAllTimeDto;
import com.didenko.gameservice.util.IgdbWrapperUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import proto.Game;
import proto.GameResult;

import java.util.ArrayList;
import java.util.List;

/*
* Provides and caches data required by the UtilRestController
* */
@RequiredArgsConstructor
@Service
public class UtilService {

    private final IgdbWrapperUtil igdbWrapperUtil;
    private IGDBWrapper igdbWrapper;
    private static final String POPULAR_PLATFORM_IDS = " where id =(6, 163, 8, 41, 9, 11, 18, 20, 167, 169, 37, 165, 390, 7, 48, 12, 49, 3, 5, 14, 19, 130);";
    private static final String SORT_ORDER = " sort generation desc;";
    private static final String GET_BEST_OF_ALL_TIME_QUERY = "fields name,rating,rating_count,cover.url;" +
            "where rating > 90;" +
            "limit 100;" +
            "sort rating_count desc;";

    @Cacheable(value = "platforms")
    public String getPlatformsList() {
        try {
            return getWrapper().apiJsonRequest(Endpoints.PLATFORMS, "fields name; exclude alternative_name; limit 500;"
                    + POPULAR_PLATFORM_IDS +
                    SORT_ORDER);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "gamesBestOfAllTime")
    public List<GameBestAllTimeDto> getBestOfAllTime() {
        try {
            byte[] gameBytes = getWrapper().apiProtoRequest(Endpoints.GAMES, GET_BEST_OF_ALL_TIME_QUERY);
            List<Game> gameList = GameResult.parseFrom(gameBytes).getGamesList();
            List<GameBestAllTimeDto> gameDtoList = new ArrayList<>();
            gameList.forEach(game -> {

                String convertedUrl = convertCoverUrl(game.getCover().getUrl(), ImageSize.HD);

                gameDtoList.add(GameBestAllTimeDto.builder()
                        .id(game.getId())
                        .cover(convertedUrl)
                        .name(game.getName())
                        .build());
            });
            return gameDtoList;
        } catch (RequestException | InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }


    private IGDBWrapper getWrapper() {
        if (igdbWrapper == null) {
            igdbWrapper = igdbWrapperUtil.getIGDBWrapper();
        }
        return igdbWrapper;
    }

    private String convertCoverUrl(String url, ImageSize size) {
        String coverUrl = url.replace("thumb", size.getSize());
        return String.format("https:%s", coverUrl);
    }
}