package com.example.EcommerceApp.product.repository;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findBySeller(User seller);
    //List<Product> findByAvailableTrue();
    Page<Product> findByAvailableTrue(Pageable page);
}
