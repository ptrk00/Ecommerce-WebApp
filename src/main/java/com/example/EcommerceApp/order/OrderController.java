package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.CannotBuyOwnProductException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.security.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/orders")
@Slf4j
@SessionAttributes("shoppingCart")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute(name = "shoppingCart")
    public List<Product> productList() {
        return new LinkedList<>();
    }

    @ModelAttribute(name = "order")
    ProductOrder order() {
        return new ProductOrder();
    }

    @GetMapping
    String getNewOrderForm() {
        return "order";
    }

    @PostMapping("/addProduct/{id}")
    String addProductToCart(@PathVariable Long id,
                                    @ModelAttribute(name = "shoppingCart") List<Product> productList,
                                    Model model) {
        log.info("Received POST request to orders/addProduct/" + id);
        var product = orderService.addProductToCart(id,productList);
        model.addAttribute("product",product);
        model.addAttribute("newRating",new ProductRating());
        return "product";
    }

    @PostMapping
    String placeOrder(@ModelAttribute(name = "order") @Valid ProductOrder order,
                      Errors errors,
                      @ModelAttribute(name = "shoppingCart") List<Product> productList,
                      SessionStatus sessionStatus,
                      @AuthenticationPrincipal User user) {

        log.info("Received POST request to /orders");

        if(errors.hasErrors()) {
            return "order";
        }

        orderService.validateProductList(productList,user);
        orderService.registerOrder(order,productList,user);

        log.info("User " + user +" has placed an order " + order);

        // session now should be closed
        sessionStatus.setComplete();

        return "home";
    }


    // this should probably be in the product controller

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException e, Model model) {
        model.addAttribute("errorMsg",e.getMessage());
        return "exception";
    }

    @ExceptionHandler(ProductAlreadyPresentInCart.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleProductAlreadyPresentInCart(ProductAlreadyPresentInCart e, Model model) {
        model.addAttribute("errorMsg",e.getMessage());
        return "exception";
    }

    @ExceptionHandler(OrderWithEmptyCartException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleOrderWithEmptyCartException(ProductNotFoundException e, Model model) {
        model.addAttribute("errorMsg", e.getMessage());
        return "exception";
    }

    @ExceptionHandler(CannotBuyOwnProductException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleCannotBuyOwnProductException(CannotBuyOwnProductException e, Model model) {
        model.addAttribute("errorMsg", e.getMessage());
        return "exception";
    }

}
