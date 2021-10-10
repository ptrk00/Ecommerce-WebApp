package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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

    @Test
    public void should_have_product_form_attribute() throws Exception {
        // given + when
        mockMvc.perform(MockMvcRequestBuilders.get("/products/new"))

                // then

                .andExpect(MockMvcResultMatchers.model().attributeExists("product"))
                .andExpect(MockMvcResultMatchers.view().name("productForm"));
    }

    @Test
    @WithMockUser
    public void should_add_rating() throws Exception {

        // given

        Long productId = 1L;

        ProductRating productRating = new ProductRating(null,5,"Super - really super","Autor",new Date());

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/products/{id}/rating",1)
                .flashAttr("rating",productRating)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/" + productId));

        verify(productService, times(1)).registerRating(productId, productRating, null);

    }

    @Test
    @WithMockUser
    public void should_register_new_product() throws Exception {

        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("Fullname");
        product.setPrice(240.0);
        product.setShortDescription("Some random short desc");
        product.setFullDescription("Some random full desc");

        when(productService.registerProduct(product,null)).thenReturn(product);

        MockMultipartFile file = new MockMultipartFile("image",new byte[]{1});

        // when

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products")
                .file(file)
        .with(csrf())
        .flashAttr("product",product))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/"+product.getId()));

        verify(productService,times(1)).registerProduct(product,null);
        verify(productService, times((1))).createImgFile(product,file);

    }



}