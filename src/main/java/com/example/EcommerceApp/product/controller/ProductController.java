package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.order.ProductOrder;
import com.example.EcommerceApp.product.model.CannotHandleFileException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.model.UnknownFileExtensionException;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.tags.form.ErrorsTag;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute(name = "newRating")
    ProductRating rating() {
        return new ProductRating();
    }

    @GetMapping
    String showAllProducts(Model model) {

        log.info("Received GET Request to /products");

       // Iterable<Product> products = productService.findAllProducts();
        Iterable<Product> products = productService.findAllAvailableProducts();
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

    // TODO: form is not sent after user is being requested to login
    // TODO: problem ProductRating has an id value but WHY?
    @PostMapping("/{id}/rating")
    public String addRating(@Valid @ModelAttribute(name = "rating") ProductRating rating,
                            Error errors,
                            @PathVariable(name="id") Long id,
                            @AuthenticationPrincipal User user,
                            Model model) {

        log.info("Received POST Request to products/" + id + "/rating/ " + rating + " from User: " + user);
        //Product product = productService.registerRating2(productService.findProduct(id), rating, user);
        Product product = productService.registerRating(id,rating,user);
        model.addAttribute("product",product);
        return "redirect:/products/"+id;
    }

    @GetMapping("/new")
    public String getNewProductForm(Model model) {
        log.info("Received GET Request to /products/new");
        model.addAttribute("product", new Product());
        return "productForm";
    }

    @PostMapping
    public String registerNewProduct(@Valid @ModelAttribute("product") Product product,
                             Errors errors,
                             @RequestParam("image") MultipartFile image,
                             @AuthenticationPrincipal User user) {

        log.info("Received POST request to /products/new with params: " + product + user);

        if(errors.hasErrors()) {
            return "productForm";
        }

        Product savedProduct = productService.registerProduct(product,user);
        productService.createImgFile(savedProduct,image);

        return "redirect:/products/" + savedProduct.getId();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException e, Model model) {
        model.addAttribute("errorMsg",e.getMessage());
        return "exception";
    }

    @ExceptionHandler(CannotHandleFileException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public String handleCannotHandleFileException(CannotHandleFileException e, Model model) {
        model.addAttribute("errorMsg",e.getMessage());
        return "exception";
    }

    @ExceptionHandler(UnknownFileExtensionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleUnknownFileExtensionException(UnknownFileExtensionException e, Model model) {
        model.addAttribute("errorMsg",e.getMessage());
        return "exception";
    }

}
