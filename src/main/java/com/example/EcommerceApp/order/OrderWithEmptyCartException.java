package com.example.EcommerceApp.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderWithEmptyCartException extends ResponseStatusException {

    public OrderWithEmptyCartException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }

}
