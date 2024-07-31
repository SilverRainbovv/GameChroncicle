package com.didenko.gameservice.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class GameTrendingDto implements Serializable {

    private long id;

    private String cover;

    private String name;
}