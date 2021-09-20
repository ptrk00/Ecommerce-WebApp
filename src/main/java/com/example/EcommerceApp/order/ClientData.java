package com.example.EcommerceApp.order;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Data
@Embeddable
public class ClientData {
    @NotBlank
    String firstName;

    @NotBlank
    String secondName;

    @NotBlank
    String streetName;

    @NotBlank
    String cityName;

    @NotBlank
    String stateName;
}
