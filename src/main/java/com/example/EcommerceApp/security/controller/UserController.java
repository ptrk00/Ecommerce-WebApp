package com.example.EcommerceApp.security.controller;

import com.example.EcommerceApp.order.ProductOrder;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getUserProfileDetails(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user",user);

        return "userDetails";
    }

    @GetMapping("/orders")
    public String getUserOrders(@AuthenticationPrincipal User user, Model model) {

        List<ProductOrder> userOrders = userService.getUserOrders(user);
        model.addAttribute("orders",userOrders);
        return "userOrders";
    }

}
