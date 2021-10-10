package com.example.EcommerceApp.product.model;

import com.example.EcommerceApp.security.model.User;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// TODO: provide a builder
// TODO: Product should have an Boolean available field
// TODO: remove product attribute class
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    public Product(String fullName,
                   Double price,
                   SellerDetails sellerDetails,
                   String shortDescription,
                   String fullDescription,
                   List<ProductRating> ratings,
                   List<ProductAttribute> productAttributes) {
        this.fullName = fullName;
        this.price = price;
        this.sellerDetails = sellerDetails;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.ratings = ratings;
        this.productAttributes = productAttributes;
    }

    public Product(String fullName,
                   Double price,
                   SellerDetails sellerDetails,
                   String shortDescription,
                   String fullDescription,
                   String imagePath) {
        this.fullName = fullName;
        this.price = price;
        this.sellerDetails = sellerDetails;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return id.equals(other.id);
    }

    public Product(String fullName,
                   Double price,
                   SellerDetails sellerDetails,
                   String shortDescription,
                   String fullDescription
                  ) {
        this.fullName = fullName;
        this.price = price;
        this.sellerDetails = sellerDetails;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
    }



    public Product() {
        this.ratings = new LinkedList<>();
        this.productAttributes = new LinkedList<>();
        available = Boolean.TRUE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Products name cannot be blank")
    String fullName;

    @Min(value = 1, message = "Products price must be greater or equal to 1$")
    Double price;

    @Embedded
    SellerDetails sellerDetails;

    @Size(max=100, message = "Products short description must be less or equal to 70 characters")
    @NotBlank
    String shortDescription;

    @Size(max=300, message = "Products description must be less or equal to 300 characters")
    @NotBlank
    String fullDescription;

    @Nullable
    String imagePath;

    @ToString.Exclude // prevent circular dependency
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductRating> ratings;

    @ToString.Exclude // prevent circular dependency
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductAttribute> productAttributes;

    @ToString.Exclude
    @ManyToOne
    User seller;

    Boolean available;

    public void addRating(ProductRating rating) {
        ratings.add(rating);
    }

    // does not work somehow ...
//    @PostPersist
//    private void setImgPath() {
//        imagePath = id + "_" + fullName;
//    }

}
