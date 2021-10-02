package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    @Transactional
    public void should_return_product_view() throws Exception {

        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("product name");
        product.setShortDescription("short description");
        product.setFullDescription("full description");

        productRepository.save(product);

        // when

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",1))

                // then

                .andExpect(MockMvcResultMatchers.view().name("product"))
                .andExpect(MockMvcResultMatchers.model().attribute("product",product))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void should_return_products_view() throws Exception {

        // given

        Product product = new Product();
        product.setFullName("product name");
        product.setShortDescription("short description");
        product.setFullDescription("full description");

        Product product2 = new Product();
        product2.setFullName("product2 name");
        product2.setShortDescription("short2 description");
        product2.setFullDescription("full2 description");

        List<Product> productList = List.of(product,product2);

        Iterable<Product> products = productRepository.saveAll(productList);

        // when

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))

                // then

                .andExpect(MockMvcResultMatchers.view().name("products"))
                .andExpect(MockMvcResultMatchers.model().attribute("products",products))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
