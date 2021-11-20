package com.example.EcommerceApp.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TokenDoesNotExistException extends ResponseStatusException {
    public TokenDoesNotExistException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
