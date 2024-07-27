package com.didenko.gameservice.controller;

import com.didenko.gameservice.entity.GameListType;

public record AddToListParams(Long igdbId, GameListType listType, String listName, Long userId) {}
