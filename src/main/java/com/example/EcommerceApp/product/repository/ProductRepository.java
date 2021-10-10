package com.example.EcommerceApp.product.repository;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.security.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {
    List<Product> findBySeller(User seller);
}
