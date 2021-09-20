package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.security.model.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/*
    'Order' is reserved sql
    keyword. Thus the name
    of this class is ProductOrder
    in order to avoid conflicts
 */

@Entity
@Data
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    @JoinColumn(name="order_id")
    @Size(min = 1)
    private List<Product> products;

    private Date orderDate;

    @Embedded
    private ClientData clientData;

    @ToString.Exclude
    @ManyToOne
    private User user;

    @PrePersist
    public void setDate() {
        orderDate = new Date();
    }

}
