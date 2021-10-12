package com.example.EcommerceApp.product.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnknownFileExtensionException extends ResponseStatusException {
    public UnknownFileExtensionException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
