package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.CannotHandleFileException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.model.UnknownFileExtensionException;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    String showAllAvailableProducts(@RequestParam(required = false) Integer page,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) Sort.Direction sortDir,
                                    Model model) {

        log.info("Received GET Request to /products with params: page=" + page + ", sort=" + sortDir);
        int pageNum = page != null && page > 0 ? page : 0;
        String sortByProperty = sortBy != null ? sortBy : "fullName";
        Sort.Direction sortDirection = sortDir != null ? sortDir : Sort.Direction.ASC;
        Page<Product> products = productService.findAllAvailableProducts(pageNum, sortByProperty, sortDirection);

        model.addAttribute("products", products);
        int totalPages = products.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNums = IntStream.rangeClosed(0, totalPages-1).boxed()
                                        .collect(Collectors.toList());
            model.addAttribute("pageNums", pageNums);
        }

        model.addAttribute("sortBy", sortByProperty);
        model.addAttribute("sortDir", sortDirection);

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
