package com.example.EcommerceApp.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

// TODO: remove embeddable. this should be relation
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerDetails {
    private String country;
    private String city;
}
