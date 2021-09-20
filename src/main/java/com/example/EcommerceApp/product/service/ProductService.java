package com.example.EcommerceApp.product.service;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> findAllProducts() {

        Iterable<Product> products = productRepository.findAll();

//        products.forEach(p -> System.out.println(p.getRatings()));
//        products.forEach(p -> System.out.println(p.getProductAttributes()));


        return products;
        // return productRepository.findAll();
    }

    public Optional<Product> findProduct(Long id) {
        return productRepository.findById(id);
    }

}
