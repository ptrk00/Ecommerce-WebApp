package com.example.EcommerceApp.order;

import com.example.EcommerceApp.configuration.FlywayConfiguration;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerIntegrationTest {

    // mock Mvc is configured in @BeforeEach method
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // inject controller
    @Autowired
    private OrderController orderController;


    // inject repos

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;


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
                .standaloneSetup(orderController)
                .setCustomArgumentResolvers(putAuthenticationPrincipal)
                .build();
    }


    @Test
    @WithMockUser
    @Transactional
    public void should_register_order() throws Exception {

        // given

        // order with client data

        ProductOrder productOrder = new ProductOrder();
        ClientData clientData = new ClientData();
        clientData.setFirstName("firstname");
        clientData.setSecondName("secondname");
        clientData.setCityName("city");
        clientData.setStateName("state");
        clientData.setStreetName("street");
        productOrder.setClientData(clientData);

        // product

        Product product = new Product();
        product.setFullName("product name");
        product.setShortDescription("short description");
        product.setFullDescription("full description");

        // current shopping cart

        List<Product> productList = new LinkedList<>();
        productList.add(product);

        // save model objects in the db

        productRepository.save(product);
        userRepository.save(user);


        // when

        mockMvc.perform(post("/orders")
                .flashAttr("order",productOrder)
                .with(csrf())
                .sessionAttr("shoppingCart",productList))
                .andDo(print())

                // then

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"));

        List<ProductOrder> found = orderRepository.findByUser(user);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getClientData()).isEqualTo(productOrder.getClientData());

    }

    @Test
    @WithMockUser
    @Transactional
    public void should_validate_order() throws Exception {

        // given

        // order with client data

        ProductOrder productOrder = new ProductOrder();
        ClientData clientData = new ClientData();
        clientData.setFirstName("firstname");
        clientData.setSecondName("secondname");

        // invalid city name
        clientData.setCityName("");

        clientData.setStateName("state");
        clientData.setStreetName("street");
        productOrder.setClientData(clientData);

        // product

        Product product = new Product();
        product.setFullName("product name");
        product.setShortDescription("short description");
        product.setFullDescription("full description");

        // current shopping cart

        List<Product> productList = new LinkedList<>();
        productList.add(product);

        // save model objects in the db

        productRepository.save(product);
        userRepository.save(user);


        // when

        mockMvc.perform(post("/orders")
                .flashAttr("order",productOrder)
                .with(csrf())
                .sessionAttr("shoppingCart",productList))
                .andDo(print())

                // then

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order"))
                .andExpect(MockMvcResultMatchers.model().hasErrors());

    }

}
