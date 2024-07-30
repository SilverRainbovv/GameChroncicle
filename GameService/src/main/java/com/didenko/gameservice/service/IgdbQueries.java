package com.didenko.gameservice.service;

import lombok.Getter;

@Getter
public enum IgdbQueries {

    POPULAR_PLATFORM_IDS("fields name; exclude alternative_name;" +
            " limit 50;" +
            " where id =(6, 163, 8, 41, 9, 11, 18, 20, 167, 169, 37, 165, 390, 7, 48, 12, 49, 3, 5, 14, 19, 130);" +
            " sort generation desc;"),
    GAMES_BEST_OF_ALL_TIME_QUERY("fields name,rating,rating_count,cover.url;" +
            "where rating > 90;" +
            "limit 100;" +
            "sort rating_count desc;"),
    GAMES_MOST_ANTICIPATED_QUERY("fields id, cover.url, hypes, release_dates.date, name, status; " +
            "where release_dates.date > %s; " +
            "sort hypes desc; " +
            "limit 100;");


    private final String qeury;

    IgdbQueries(String qeury) {
        this.qeury = qeury;
    }
}
