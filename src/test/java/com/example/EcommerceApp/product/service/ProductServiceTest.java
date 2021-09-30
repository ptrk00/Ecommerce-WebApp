package com.example.EcommerceApp.product.service;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void should_throw_when_given_invalid_id() {

        // given

        Long badId = 5L;

        when(productRepository.findById(badId)).thenReturn(Optional.empty());

        // when + then

        assertThatThrownBy(() -> {
            productService.findProduct(badId);
        }).isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(Long.toString(badId));
    }

    @Test
    void should_return_product_when_given_valid_id() {

        // given

        Long id = 5L;
        String productName = "Product's full name";
        Product product = new Product();
        product.setFullName(productName);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // when

        Product returned = productService.findProduct(id);

        // then

        assertThat(returned.getFullName()).isEqualTo(productName);

    }

    @Test
    void should_not_throw_when_given_valid_id() {

        // given

        Long id = 5L;
        String productName = "Product's full name";
        Product product = new Product();
        product.setFullName(productName);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // when

        Throwable throwable = catchThrowable(() -> productService.findProduct(id));

        // then

        assertThat(throwable).isNull();


    }

}