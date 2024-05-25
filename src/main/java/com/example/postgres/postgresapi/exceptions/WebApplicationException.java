package com.example.postgres.postgresapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WebApplicationException extends RuntimeException {
    public WebApplicationException(String message) {
        super(message);
    }
}
