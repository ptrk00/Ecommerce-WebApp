package com.example.EcommerceApp.product.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;


@Embeddable
@Data
@NoArgsConstructor
public class SellerDetails {

    public SellerDetails(String companyName, String companyHeadquartersLocation, String customerServiceNumber) {
        this.companyName = companyName;
        this.companyHeadquartersLocation = companyHeadquartersLocation;
        this.customerServiceNumber = customerServiceNumber;
    }

    private String companyName;
    private String companyHeadquartersLocation;
    private String customerServiceNumber;
}
