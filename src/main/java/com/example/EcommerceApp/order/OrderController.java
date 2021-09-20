package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.security.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        Optional<Product> product = orderService.findProduct(id);
        product.ifPresent(productList::add);
        log.info("Added " + product.get() + " to the cart");
//        String referer = request.getParameter("Referer");
//        return "redirect:" + referer;
        model.addAttribute("product",product.get());
        return "product";
    }

    @PostMapping
    String placeOrder(@ModelAttribute(name = "order") @Valid ProductOrder order,
                      Errors errors,
                      @ModelAttribute(name = "shoppingCart") List<Product> productList,
                      SessionStatus sessionStatus,
                      @AuthenticationPrincipal User user) {

        if(errors.hasErrors()) {
            return "order";
        }
//
//        order.setProducts(productList);
//        order.setUser(user);
//
//        orderService.registerOrder(order,user);
        orderService.registerOrderTransactional(order,productList,user);

        sessionStatus.setComplete();
        log.info("User " + user +" has placed an order " + order);

        return "home";
    }

}
