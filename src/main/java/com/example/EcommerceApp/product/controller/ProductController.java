package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    String showAllProducts(Model model) {

        log.info("Received GET Request");

        Iterable<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        return "products";
    }

    @GetMapping("/{id}")
    String showProduct(@PathVariable Long id, Model model) {

        log.info("Received GET Request");

        Optional<Product> productOptional = productService.findProduct(id);

        model.addAttribute("product",productOptional.get());

        return "product";
    }

}
