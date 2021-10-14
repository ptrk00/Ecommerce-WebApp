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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerIntegrationTest {

    @Value("${app.imagesPath}")
    private String imagesPath;

    private MockMvc mockMvc;

    @Autowired
    ProductController productController;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebApplicationContext webApplicationContext;


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
    public void should_return_available_products_view() throws Exception {

        // given

        Product product = new Product();
        product.setFullName("product name");
        product.setShortDescription("short description");
        product.setFullDescription("full description");

        Product product2 = new Product();
        product2.setFullName("product2 name");
        product2.setShortDescription("short2 description");
        product2.setFullDescription("full2 description");

        // mark this one as unavailable
        Product product3 = new Product();
        product3.setAvailable(Boolean.FALSE);
        product3.setFullName("product3 name");
        product3.setShortDescription("short3 description");
        product3.setFullDescription("full3 description");

        List<Product> availList = List.of(product,product2);
        List<Product> allProducts = List.of(product,product2,product3);

        Iterable<Product> products = productRepository.saveAll(allProducts);

        // when
        // '/' character at the end of url gets rid of circular view path exception
        // which may be caused by custom mock mvc setup (?)
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/products/"))

                // then

                .andExpect(MockMvcResultMatchers.view().name("products"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Page<Product> returned = (Page<Product>) resultActions.andReturn().getModelAndView().getModel().get("products");
        assertThat(returned.get().collect(Collectors.toList())).hasSize(2);
        assertThat(returned.get().collect(Collectors.toList())).containsAll(availList);
    }

    // this is written as integration test in order to let spring inject
    // @Value field which contains path to the images
    @Test
    @Transactional
    public void should_generate_file() throws IOException {

        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("product1");
        product.setShortDescription("some short description");
        product.setFullDescription("some fulll description");

        byte[] buff = {0,1,2,3};

        MultipartFile mock = new MockMultipartFile("somerandomname.jpg", buff) {
            @Override
            public String getOriginalFilename() {
                return "somerandomname.jpg";
            }
        };

        File file = new File(imagesPath + "/" + product.getId() + "_" + product.getFullName() + ".jpg");


        // when

        productService.createImgFile(product,mock);

        // then

        assertThat(file.exists()).isTrue();

        // delete created file
        Files.delete(file.toPath());

    }

    @Test
    @WithMockUser
    @Transactional
    public void should_register_new_product() throws Exception {


        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("Fullname");
        product.setPrice(240.0);
        product.setShortDescription("Some random short desc");
        product.setFullDescription("Some random full desc");

        byte[] buff = {0,1,2,3};
        MockMultipartFile file = new MockMultipartFile("image","somerandomname.jpg", "multipart",buff) {
            @Override
            public String getOriginalFilename() {
                return "somerandomname.jpg";
            }
        };

        // problem with mocking this method
        userRepository.save(user);

        // when

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.multipart("/products")
                .file(file)
                .with(csrf())
                .flashAttr("product", product))
                .andDo(print());
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/products/"+product.getId()));

        var all = productService.findAllProducts();
        assertThat(all).hasSize(1);
        Product found = productService.findProduct(all.iterator().next().getId());

        File filePath = new File(imagesPath + "/" +found.getId() + "_" + found.getFullName() + ".jpg");

        assertThat(found.getFullName()).isEqualTo(product.getFullName());
        assertThat(found.getAvailable()).isTrue();
        assertThat(filePath.exists()).isTrue();
        assertThat(response.andReturn().getResponse().getRedirectedUrl()).isEqualTo("/products/" + found.getId());
        // delete created file
        Files.delete(filePath.toPath());
    }

}
