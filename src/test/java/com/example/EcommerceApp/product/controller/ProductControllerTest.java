package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;


    @Test
    public void should_return_view_with_product_info_when_valid_id() throws Exception {

        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("Fullname");

        when(productService.findProduct(1L)).thenReturn(product);

        // when

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))

                // then

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product"))
                .andExpect(MockMvcResultMatchers.model().attribute("product",product));

    }

    @Test
    public void should_catch_exception_and_return_404_and_return_exception_view_when_invalid_id() throws Exception {

        // given

        when(productService.findProduct(any(Long.class))).thenThrow(ProductNotFoundException.class);

        // when

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))

                // then

                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.view().name("exception"));
    }

}