package com.didenko.gameservice.repository;

import com.didenko.gameservice.entity.GameList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameList, Long> {}