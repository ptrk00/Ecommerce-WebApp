package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
//@Validated
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;


    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Transactional
    public void registerOrder(ProductOrder order, List<Product> productList, User user) {

        // product list cannot be empty

        if(productList.isEmpty())
            throw new OrderWithEmptyCartException("Order must contain at least one product");

        order.setProducts(productList);
        order.setUser(user);
        user.addOrder(order);
        orderRepository.save(order);

    }

    public Product addProductToCart(Long id, List<Product> productList) {

        Product product = findProduct(id);

        // if product with given id is already present in cart

        if(productList.contains(product))
            throw new ProductAlreadyPresentInCart("Product with id " + id + " is already present in the cart");

        productList.add(product);
        log.info("Added " + product + " to the cart");

        return product;

    }

    public Product findProduct(Long id) {
        return productService.findProduct(id);
    }

}
