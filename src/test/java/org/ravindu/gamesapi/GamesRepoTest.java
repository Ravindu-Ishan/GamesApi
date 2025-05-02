package org.ravindu.gamesapi;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.ravindu.gamesapi.model.Games;
import org.ravindu.gamesapi.repository.GamesRepo;

import static org.junit.jupiter.api.Assertions.*;

@Log
@Testcontainers
@SpringBootTest
public class GamesRepoTest {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private GamesRepo gamesRepo;

    @BeforeEach
    void setUp() {
        log.info("Clearing the games table before test.");
        gamesRepo.deleteAll();
    }

    @Test
    void testCreateGameShouldSucceed() {
        log.info("Running testCreateGameShouldSucceed...");
        Games game = new Games();
        game.setTitle("Halo");
        game.setPublisher("Microsoft");
        game.setDescription("A sci-fi shooter");
        game.setGenre("Shooter");
        game.setReleaseDate("2023-01-01");
        game.setCoverImageURL("http://example.com/halo.jpg");
        game.setDateCreated(java.time.LocalDateTime.now().toString());

        log.info("Saving new game: " + game.getTitle());
        gamesRepo.save(game);

        Games savedGame = gamesRepo.findById(game.getId()).orElseThrow();
        log.info("Saved game retrieved: " + savedGame);

        assertEquals("Halo", savedGame.getTitle());
        assertEquals("Microsoft", savedGame.getPublisher());
    }

    @Test
    void testCreateGameWithDuplicateTitleShouldThrow() {
        log.info("Running testCreateGameWithDuplicateTitleShouldThrow...");

        Games game1 = new Games();
        game1.setTitle("Halo");
        game1.setPublisher("Microsoft");
        game1.setDescription("A sci-fi shooter");
        game1.setGenre("Shooter");
        game1.setReleaseDate("2023-01-01");
        game1.setCoverImageURL("http://example.com/halo.jpg");
        game1.setDateCreated(java.time.LocalDateTime.now().toString());

        log.info("Saving first game with title: " + game1.getTitle());
        gamesRepo.save(game1);

        Games game2 = new Games();
        game2.setTitle("Halo");
        game2.setPublisher("Microsoft");
        game2.setDescription("Another sci-fi shooter");
        game2.setGenre("Shooter");
        game2.setReleaseDate("2023-02-01");
        game2.setCoverImageURL("http://example.com/halo2.jpg");
        game2.setDateCreated(java.time.LocalDateTime.now().toString());

        log.info("Attempting to save second game with duplicate title: " + game2.getTitle());
        Exception ex = assertThrows(DataIntegrityViolationException.class, () -> gamesRepo.save(game2));
        log.warning("Expected duplicate title exception occurred: " + ex.getMessage());
    }

    @Test
    void testCreateGameWithMissingFieldsShouldFail() {
        log.info("Running testCreateGameWithMissingFieldsShouldFail...");
        Games game = new Games();
        game.setTitle("");
        game.setPublisher("");
        game.setGenre("");

        try {
            gamesRepo.save(game);
            fail("Expected exception due to missing required fields");
        } catch (Exception e) {
            Throwable rootCause = e;
            while (rootCause.getCause() != null) {
                rootCause = rootCause.getCause();
            }
            log.warning("Validation failed as expected. Root cause: " + rootCause.getClass().getName());
            assertTrue(rootCause instanceof ConstraintViolationException,
                    "Root cause should be ConstraintViolationException but was " + rootCause.getClass().getName());
        }
    }

    @Test
    void testDefaultReleaseDateIsApplied() {
        log.info("Running testDefaultReleaseDateIsApplied...");

        Games game = new Games();
        game.setTitle("New Game");
        game.setPublisher("Publisher");
        game.setGenre("Genre");
        game.setDateCreated(java.time.LocalDateTime.now().toString());

        log.info("Saving game without releaseDate...");
        gamesRepo.save(game);

        Games savedGame = gamesRepo.findById(game.getId()).orElseThrow();
        log.info("Retrieved game: " + savedGame);
        assertEquals("TBA", savedGame.getReleaseDate(), "Default release date should be 'TBA'");
    }

    @Test
    void testDateCreatedIsNotUpdatable() {
        log.info("Running testDateCreatedIsNotUpdatable...");

        Games game = new Games();
        game.setTitle("Test Game");
        game.setPublisher("Test Publisher");
        game.setGenre("Test Genre");
        String originalDate = java.time.LocalDateTime.now().toString();
        game.setDateCreated(originalDate);

        log.info("Saving game with original dateCreated: " + originalDate);
        gamesRepo.save(game);

        Games savedGame = gamesRepo.findById(game.getId()).orElseThrow();
        String newDate = java.time.LocalDateTime.now().plusDays(1).toString();
        log.info("Attempting to update dateCreated to: " + newDate);
        savedGame.setDateCreated(newDate);
        gamesRepo.save(savedGame);

        Games retrievedGame = gamesRepo.findById(game.getId()).orElseThrow();
        log.info("Final retrieved dateCreated: " + retrievedGame.getDateCreated());
        assertEquals(originalDate, retrievedGame.getDateCreated(),
                "dateCreated should not be updatable");
    }
}
