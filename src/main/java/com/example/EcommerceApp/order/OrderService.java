package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

//    private final OrderRepository orderRepository;
//    private final ProductService productService;
//
//    public OrderService(OrderRepository orderRepository, ProductService productService) {
//        this.orderRepository = orderRepository;
//        this.productService = productService;
//    }

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

//
//    @PersistenceContext
//    @Autowired
//    private EntityManager entityManager;

    public OrderService(OrderRepository orderRepository, ProductService productService,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public void registerOrder(ProductOrder order, User user) {

        user = userRepository.findByUsername(user.getUsername());
        user.addOrder(order);

     //   entityManager.merge(user);
    //    user.addOrder(order);

        orderRepository.save(order);
    }

    @Transactional
    public void registerOrderTransactional(ProductOrder order, List<Product> productList, User user) {

        order.setProducts(productList);
        order.setUser(user);
        user.addOrder(order);
        orderRepository.save(order);

    }

    public Optional<Product> findProduct(Long id) {
        return productService.findProduct(id);
    }

}
