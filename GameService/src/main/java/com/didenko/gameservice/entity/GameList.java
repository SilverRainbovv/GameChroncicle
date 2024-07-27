package com.didenko.gameservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Table(name = "gamelist")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameList {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long userId;

    private String UID;

    private String name;

    @Enumerated(EnumType.STRING)
    private GameListType type;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "gamelist_game", joinColumns = {@JoinColumn(name = "list_id")},
    inverseJoinColumns = {@JoinColumn(name = "game_id")})
    private Set<Game> gameIds = new HashSet<>();

    public void addGame(Game game) {
        this.gameIds.add(game);
    }

    public void removeGame(Game game) {
        this.gameIds.remove(game);
    }
}