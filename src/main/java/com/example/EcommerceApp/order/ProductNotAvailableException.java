package com.example.EcommerceApp.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductNotAvailableException extends ResponseStatusException {

    public ProductNotAvailableException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }

}
