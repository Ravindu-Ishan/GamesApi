package org.ravindu.gamesapi.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String id) {
        super("Game not found with id: " + id);
    }
}
