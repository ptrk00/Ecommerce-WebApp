package com.example.EcommerceApp.product.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CannotHandleFileException extends ResponseStatusException {
    public CannotHandleFileException(String reason) {
        super(HttpStatus.FAILED_DEPENDENCY, reason);
    }
}
