package org.ravindu.gamesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private ZonedDateTime timestamp;
    private String path;
}
