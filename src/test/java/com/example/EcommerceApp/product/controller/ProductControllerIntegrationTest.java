package com.example.EcommerceApp.product.controller;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.product.service.ProductService;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    ProductController productController;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;


    // user to be injected
    private final User user = new User("user1","12345","email@email.com");

    // enables to inject @AuthenticationPrincipal in controller's method
    private final HandlerMethodArgumentResolver putAuthenticationPrincipal = new HandlerMethodArgumentResolver() {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(User.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            return user;
        }
    };

    // setup mockMvc so it will inject user as @AuthenticationPrincipal
    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal)
                .build();
    }



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
    @Disabled
    // TODO: Circular view path error, its caused because of different mock mvc setup
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

    @Test
    @WithMockUser
    @Transactional
    @Disabled
    // TODO: finish
    public void should_register_new_product() throws Exception {


        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("Fullname");
        product.setPrice(240.0);
        product.setShortDescription("Some random short desc");
        product.setFullDescription("Some random full desc");


        MockMultipartFile file = new MockMultipartFile("image",new byte[]{1});

        // problem with mocking this method
        userRepository.save(user);

        // when

        mockMvc.perform(MockMvcRequestBuilders.multipart("/products")
                .file(file)
                .with(csrf())
                .flashAttr("product",product))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/"+product.getId()));

        assertThat(productService.findProduct(product.getId())).isEqualTo(product);

    }

}
