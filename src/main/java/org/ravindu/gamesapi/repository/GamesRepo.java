package org.ravindu.gamesapi.repository;

import org.ravindu.gamesapi.model.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GamesRepo extends JpaRepository<Games, UUID> {
    // Optional: Add custom queries here if needed
}
