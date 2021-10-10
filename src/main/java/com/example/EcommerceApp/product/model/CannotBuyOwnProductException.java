package com.example.EcommerceApp.product.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CannotBuyOwnProductException extends ResponseStatusException {
    public CannotBuyOwnProductException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
