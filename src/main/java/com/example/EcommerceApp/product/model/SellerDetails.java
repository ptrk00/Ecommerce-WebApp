package com.example.EcommerceApp.product.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Contains seller details like company name.
 * Is part of a main Product class.
 */

@Embeddable
@Data
@RequiredArgsConstructor
public class SellerDetails {

    // TODO: why lombok is not generating this?
    public SellerDetails(String companyName, String companyHeadquartersLocation, String customerServiceNumber) {
        this.companyName = companyName;
        this.companyHeadquartersLocation = companyHeadquartersLocation;
        this.customerServiceNumber = customerServiceNumber;
    }

    private String companyName;
    private String companyHeadquartersLocation;
    private String customerServiceNumber;
}
