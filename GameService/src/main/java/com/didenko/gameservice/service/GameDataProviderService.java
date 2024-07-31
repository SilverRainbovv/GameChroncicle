package com.didenko.gameservice.service;

import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.utils.Endpoints;
import com.didenko.gameservice.dto.GameMostAnticipatedDto;
import com.didenko.gameservice.dto.GameTrendingDto;
import com.didenko.gameservice.dto.ImageSize;
import com.didenko.gameservice.dto.GameBestAllTimeDto;
import com.didenko.gameservice.util.IgdbWrapperUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import proto.Game;
import proto.GameResult;
import proto.ReleaseDate;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/*
 * Provides and caches data required by the UtilRestController
 * */
@RequiredArgsConstructor
@Service
public class GameDataProviderService {

    private final IgdbWrapperUtil igdbWrapperUtil;
    private IGDBWrapper igdbWrapper;

    @Cacheable(value = "platforms")
    public String getPlatformsList() {
        try {
            return getWrapper().apiJsonRequest(Endpoints.PLATFORMS, IgdbQueries.POPULAR_PLATFORM_IDS.getQuery());
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "gamesBestOfAllTime")
    public List<GameBestAllTimeDto> getBestOfAllTime() {
        try {
            byte[] gamesBytes = getWrapper().apiProtoRequest(Endpoints.GAMES, IgdbQueries.GAMES_BEST_OF_ALL_TIME_QUERY.getQuery());
            List<Game> gameList = GameResult.parseFrom(gamesBytes).getGamesList();
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

    @Cacheable(value = "gamesTrending")
    public List<GameTrendingDto> getTrendingGames() {
        long currentTimeTimestamp = Instant.now().getEpochSecond();
        long halfYearBackTimestamp = Instant.now().minus(Duration.ofDays(6 * 30)).getEpochSecond();

        String query = IgdbQueries.GAMES_TRENDING_NOW_QUERY.getQuery();
        query = String.format(query, currentTimeTimestamp, halfYearBackTimestamp);

        List<GameTrendingDto> gameTrendingDtoList = new ArrayList<>();

        try {
            byte[] gamesBytes = getWrapper().apiProtoRequest(Endpoints.GAMES, query);
            List<Game> gameList = GameResult.parseFrom(gamesBytes).getGamesList();

            gameList.forEach(game -> {
                String convertedUrl = convertCoverUrl(game.getCover().getUrl(), ImageSize.HD);

                Optional<Long> earliestReleaseDate = findEarliestReleaseDate(game);

                if (earliestReleaseDate.isPresent() && earliestReleaseDate.get() > halfYearBackTimestamp) {
                    gameTrendingDtoList.add(GameTrendingDto.builder()
                            .id(game.getId())
                            .cover(convertedUrl)
                            .name(game.getName())
                            .build());
                }
            });

        } catch (RequestException | InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        return gameTrendingDtoList.stream().limit(5).toList();
    }

    @Cacheable(value = "gamesMostAnticipated")
    public List<GameMostAnticipatedDto> getMostAnticipatedGames() {
        long currentTimeTimestamp = Instant.now().getEpochSecond();

        String query = IgdbQueries.GAMES_MOST_ANTICIPATED_QUERY.getQuery();
        query = String.format(query, currentTimeTimestamp);

        List<GameMostAnticipatedDto> gameDtoList = new ArrayList<>();

        try {
            byte[] gamesBytes = getWrapper().apiProtoRequest(Endpoints.GAMES, query);
            List<Game> gameList = GameResult.parseFrom(gamesBytes).getGamesList();

            gameList.forEach(game -> {

                String convertedUrl = convertCoverUrl(game.getCover().getUrl(), ImageSize.HD);

                Optional<Long> earliestReleaseDate = findEarliestReleaseDate(game);

                if (earliestReleaseDate.isPresent() && earliestReleaseDate.get() > currentTimeTimestamp) {
                    String formattedReleaseDate = new SimpleDateFormat("dd MMM yyyy").format(earliestReleaseDate.get() * 1000);
                    gameDtoList.add(GameMostAnticipatedDto.builder()
                            .id(game.getId())
                            .cover(convertedUrl)
                            .name(game.getName())
                            .releaseDate(formattedReleaseDate)
                            .build());
                }
            });
        } catch (RequestException | InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        return gameDtoList.stream().limit(5).toList();
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

    private Optional<Long> findEarliestReleaseDate(Game game) {
        return game.getReleaseDatesList().stream()
                .map(ReleaseDate::getDate)
                .map(Timestamp::getSeconds)
                .min(Long::compare);
    }
}