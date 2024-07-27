package com.didenko.gameservice.entity;

import lombok.Getter;

@Getter
public enum GameListType {

    WISHLIST("WISHLIST"), COLLECTION("COLLECTION"), OTHER("");

    private final String name;

    GameListType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}