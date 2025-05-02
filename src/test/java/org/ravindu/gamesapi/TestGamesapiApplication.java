package org.ravindu.gamesapi;

import org.springframework.boot.SpringApplication;

public class TestGamesapiApplication {

	public static void main(String[] args) {
		SpringApplication.from(GamesapiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
