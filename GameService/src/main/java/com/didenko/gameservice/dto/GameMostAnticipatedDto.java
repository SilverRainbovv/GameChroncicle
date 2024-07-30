package com.didenko.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMostAnticipatedDto implements Serializable {

    private long id;

    private String cover;

    private String name;

    private String releaseDate;

}