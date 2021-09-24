package com.example.EcommerceApp.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductNotFoundException extends ResponseStatusException {

    public ProductNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

}
