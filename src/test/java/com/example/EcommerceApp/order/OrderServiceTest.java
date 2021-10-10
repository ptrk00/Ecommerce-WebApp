package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.CannotBuyOwnProductException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void should_register_valid_order() {

        // given

        Product product = new Product();
        product.setFullName("product's fullname");
        List<Product> products = List.of(
                product
        );


        ClientData clientData = new ClientData();
        clientData.setFirstName("first name");

        User user = new User("username","passwd","email@em.pl");

        ProductOrder order = new ProductOrder();
        order.setClientData(clientData);

        when(orderRepository.save(order)).thenReturn(order);

        // when

        orderService.registerOrder(order,products,user);

        // then

        verify(orderRepository,times(1)).save(order);

    }

    @Test
    public void should_throw_when_empty_cart() {

        // given

        List<Product> products = new LinkedList<>();

        ClientData clientData = new ClientData();
        clientData.setFirstName("first name");

        User user = new User("username","passwd","email@em.pl");

        ProductOrder order = new ProductOrder();
        order.setClientData(clientData);

        // when + then

        assertThatThrownBy(() -> {
            orderService.registerOrder(order, products, user);
        }).isInstanceOf(OrderWithEmptyCartException.class);

        verify(orderRepository,times(0)).save(order);

    }

    @Test
    public void should_throw_when_product_is_already_present_in_cart() {

        // given

        Long dupId = 1L;

        Product product = new Product();
        product.setId(dupId);
        product.setFullName("product's fullname");
        List<Product> products = List.of(
                product
        );

        when(productService.findProduct(1L)).thenReturn(product);

        // when + then

        assertThatThrownBy(() -> {
            orderService.addProductToCart(dupId,products);
        }).isInstanceOf(ProductAlreadyPresentInCart.class).hasMessageContaining(Long.toString(dupId));
    }

    @Test
    public void should_add_product_to_cart() {

        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("product's fullname");
        List<Product> products = new ArrayList<>();
        products.add(product);

        Long id = 2L;

        Product newProduct = new Product();
        newProduct.setId(id);
        newProduct.setFullName("product 2");

        when(productService.findProduct(id)).thenReturn(newProduct);

        // when

        orderService.addProductToCart(id,products);

        // then

        assertThat(products).contains(newProduct);

    }

    @Test
    public void should_throw_when_trying_to_buy_own_product() {

        // given

        Product product1 = new Product();
        product1.setId(1L);
        product1.setFullName("product1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setFullName("product2");

        List<Product> cart = List.of(product1,product2);
        List<Product> own = List.of(product1);

        User user = new User("usrnme","passwd","em@em.em");

        when(productService.findByUser(user)).thenReturn(own);

        // when

        assertThatThrownBy(() -> {
            orderService.validateProductList(cart,user);
        }).isInstanceOf(CannotBuyOwnProductException.class).hasMessageContaining(Long.toString(product1.getId()));

    }


}