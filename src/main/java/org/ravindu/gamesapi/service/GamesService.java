package org.ravindu.gamesapi.service;

import org.ravindu.gamesapi.dto.GamesDto;
import org.ravindu.gamesapi.exception.DuplicateGameTitleException;
import org.ravindu.gamesapi.exception.GameNotFoundException;
import org.ravindu.gamesapi.model.Games;
import org.ravindu.gamesapi.repository.GamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GamesService {

    @Autowired
    private GamesRepo gamesRepo;


    // inside your createGame method:
    public Games createGame(Games game) {
        game.setDateCreated(java.time.LocalDateTime.now().toString());
        game.setDateModified(java.time.LocalDateTime.now().toString());

        try {
            return gamesRepo.save(game);
        } catch (DataIntegrityViolationException ex) {
            // This is the actual Spring exception thrown for unique constraint violations
            throw new DuplicateGameTitleException(game.getTitle());
        }
    }

    public Games patchGame(UUID id, GamesDto dto) {
        Games existing = gamesRepo.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id.toString()));

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getPublisher() != null) existing.setPublisher(dto.getPublisher());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getGenre() != null) existing.setGenre(dto.getGenre());
        if (dto.getReleaseDate() != null) existing.setReleaseDate(dto.getReleaseDate());
        if (dto.getCoverImageURL() != null) existing.setCoverImageURL(dto.getCoverImageURL());

        existing.setDateModified(java.time.LocalDateTime.now().toString());
        return gamesRepo.save(existing);
    }

    public List<Games> getAllGames() {
        return gamesRepo.findAll();
    }

    public Games getGameById(UUID id) {
        return gamesRepo.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id.toString()));
    }

    public void deleteGameById(UUID id) {
        if (!gamesRepo.existsById(id)) {
            throw new GameNotFoundException(id.toString());
        }
        gamesRepo.deleteById(id);
    }
}
