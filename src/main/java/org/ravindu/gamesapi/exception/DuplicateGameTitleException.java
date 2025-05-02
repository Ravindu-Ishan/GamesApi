package org.ravindu.gamesapi.exception;

public class DuplicateGameTitleException extends RuntimeException {
    public DuplicateGameTitleException(String title) {
        super("A game with title '" + title + "' already exists.");
    }
}
