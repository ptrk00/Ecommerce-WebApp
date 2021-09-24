package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        log.info("Received GET Request to /products");

        Iterable<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        return "products";
    }

    @GetMapping("/{id}")
    String showProduct(@PathVariable Long id, Model model) {

        log.info("Received GET Request to /products/" + id);

        Product product = productService.findProduct(id);

        model.addAttribute("product",product);

        return "product";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException e, Model model) {
        model.addAttribute("errorMsg",e.getMessage());
        return "exception";
    }

}
