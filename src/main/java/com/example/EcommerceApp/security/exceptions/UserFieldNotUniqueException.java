package com.example.EcommerceApp.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class UserFieldNotUniqueException extends ResponseStatusException {
    public UserFieldNotUniqueException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
