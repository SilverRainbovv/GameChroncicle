package com.didenko.gameservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Game {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NaturalId
    private Long igdbId;

    @Builder.Default
    @ManyToMany(mappedBy = "gameIds")
    private Set<GameList> gameLists = new HashSet<>();
}