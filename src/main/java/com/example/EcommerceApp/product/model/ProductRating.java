package com.example.EcommerceApp.product.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductRating {

    public ProductRating(Product product, int starNum, String description, String author, Date postedOn) {
        this.product = product;
        this.starNum = starNum;
        this.description = description;
        this.author = author;
        this.postedOn = postedOn;
    }

    public ProductRating() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @Range(min=1,max=5, message = "Rating should be number in range 1-5")
    private int starNum;

    @Size(min = 10, max = 100, message = "Description should be minimum 10 characters and maximum 100 characters")
    String description;

    @NotBlank
    String author;

    Date postedOn;

    @PrePersist
    void setDate() {
        postedOn = new Date();
    }

}
