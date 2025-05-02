package org.ravindu.gamesapi.exception;

import lombok.extern.java.Log;
import org.ravindu.gamesapi.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log
@ControllerAdvice
public class GlobalExceptionHandler {

    // For IllegalArguments such as invalid file path
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> illegalArgumentException(Exception ex) {
        log.warning("An error occured : " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(400 ,ex.getMessage());
        return ResponseEntity.status(400).body(errorResponse);
    }
}