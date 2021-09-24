package com.example.EcommerceApp.product.service;

import com.example.EcommerceApp.order.ProductNotFoundException;
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
        return productRepository.findAll();
    }

    public Product findProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if(optProduct.isEmpty())
            throw new ProductNotFoundException("Product with id " + id + " has not been found");
        return optProduct.get();
    }

}
