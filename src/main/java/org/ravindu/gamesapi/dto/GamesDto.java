package org.ravindu.gamesapi.dto;

import lombok.Data;

@Data
public class GamesDto {
    private String title;
    private String publisher;
    private String description;
    private String genre;
    private String releaseDate;
    private String coverImageURL;
}

