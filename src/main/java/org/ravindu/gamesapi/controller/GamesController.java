package org.ravindu.gamesapi.controller;

import org.ravindu.gamesapi.dto.GamesDto;
import org.ravindu.gamesapi.model.Games;
import org.ravindu.gamesapi.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GamesController {

    @Autowired
    private GamesService gamesService;

    @PostMapping
    public ResponseEntity<Games> createGame(@RequestBody Games game) {
        Games createdGame = gamesService.createGame(game);
        return ResponseEntity.ok(createdGame);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Games> updateGame(@PathVariable UUID id, @RequestBody GamesDto game) {
        Games updatedGame = gamesService.patchGame(id,game);
        return ResponseEntity.ok(updatedGame);
    }

    @GetMapping
    public ResponseEntity<List<Games>> getAllGames() {
        return ResponseEntity.ok(gamesService.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Games> getGameById(@PathVariable UUID id) {
        return ResponseEntity.ok(gamesService.getGameById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID id) {
        gamesService.deleteGameById(id);
        return ResponseEntity.noContent().build();
    }

}
