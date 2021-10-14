//package com.example.EcommerceApp.product.model;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
///**
// * Contains optional
// * product attributes
// * attributeName: attributeValue
// */
//@Entity
//@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//public  class ProductAttribute {
//
//    public ProductAttribute(Product product, String attributeName, String attributeValue) {
//        this.product = product;
//        this.attributeName = attributeName;
//        this.attributeValue = attributeValue;
//    }
//
//    public ProductAttribute() {
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    Product product;
//
//    @Size(max=20)
//    String attributeName;
//
//    @Size(max=40)
//    String attributeValue;
//
//}
