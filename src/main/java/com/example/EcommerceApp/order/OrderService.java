package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.CannotBuyOwnProductException;
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

        // handle empty products list
        if(productList.isEmpty())
            throw new OrderWithEmptyCartException("Order must contain at least one product");

        productService.markAllProductsAsUnavailable(productList);

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

    public void validateProductList(List<Product> productsInCart, User user) {
        List<Product> userProducts = productService.findByUser(user);
        for(Product owned : userProducts) {
            for (Product cartP : productsInCart) {
                if (owned.equals(cartP))
                    throw new CannotBuyOwnProductException("Cannot buy own product. Duplicate id: " + cartP.getId());
            }
        }

        productsInCart.forEach(p -> {
            if(!p.getAvailable())
                throw new ProductNotAvailableException("Product with id:" + p.getId() + " is not available");
        });
    }
}
