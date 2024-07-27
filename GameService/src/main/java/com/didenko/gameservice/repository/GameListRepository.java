package com.didenko.gameservice.repository;

import com.didenko.gameservice.entity.GameList;
import com.didenko.gameservice.entity.GameListType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GameListRepository extends JpaRepository<GameList, Long> {

    Set<GameList> findByUserId(Long userId);

    Optional<GameList> findByUserIdAndType(Long userId, GameListType type);

    Optional<GameList> findByUserIdAndName(Long userId, String listName);

}