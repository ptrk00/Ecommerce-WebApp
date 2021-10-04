package com.example.EcommerceApp.security.service;

import com.example.EcommerceApp.order.OrderRepository;
import com.example.EcommerceApp.order.ProductOrder;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OrderRepository orderRepository;

    public List<ProductOrder> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

}
