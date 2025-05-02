package org.ravindu.gamesapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.ravindu.gamesapi.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.ZonedDateTime;

@Log
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildResponseEntity(Exception ex, HttpStatus status, HttpServletRequest request) {
        log.warning("Exception caught: " + ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                ZonedDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidGameDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidData(InvalidGameDataException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(DuplicateGameTitleException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateTitle(DuplicateGameTitleException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT, request);
    }

}
