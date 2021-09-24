package com.example.EcommerceApp.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductAlreadyPresentInCart extends ResponseStatusException {

    public ProductAlreadyPresentInCart(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }

}
