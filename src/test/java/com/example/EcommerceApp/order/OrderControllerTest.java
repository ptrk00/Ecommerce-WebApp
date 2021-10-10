package com.example.EcommerceApp.order;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;


    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());


    @Test
    public void should_redirect_to_login_when_no_authentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser
    public void should_return_view_and_OK_when_authentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order"));
    }

    @Test
    public void should_not_register_order_when_no_authentication() throws Exception {

        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(1L);
        ClientData clientData = new ClientData();
        clientData.setFirstName("firstname");
        clientData.setSecondName("secondname");
        clientData.setCityName("city");
        clientData.setStateName("state");
        clientData.setStreetName("street");
        productOrder.setClientData(clientData);

        Product product = new Product();
        product.setId(2L);
        product.setFullName("product name");
        List<Product> productList = new LinkedList<>();
        productList.add(product);

        mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(productOrder))
        .content(objectMapper.writeValueAsString(productList)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser
    public void should_register_order_when_authentication_provided() throws Exception {

        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(1L);
        ClientData clientData = new ClientData();
        clientData.setFirstName("firstname");
        clientData.setSecondName("secondname");
        clientData.setCityName("city");
        clientData.setStateName("state");
        clientData.setStreetName("street");
        productOrder.setClientData(clientData);

        Product product = new Product();
        product.setId(2L);
        product.setFullName("product name");
        List<Product> productList = new LinkedList<>();
        productList.add(product);


        mockMvc.perform(post("/orders")
                .content(objectMapper.writeValueAsString(productOrder) + ", " + objectMapper.writeValueAsString(productList))
                .sessionAttr(TOKEN_ATTR_NAME,csrfToken)
                .param(csrfToken.getParameterName(),csrfToken.getToken()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"));
    }


    @Test
    @WithMockUser
    public void should_catch_exception_when_trying_to_add_same_product_to_cart_again() throws Exception {

        // given

        Product product = new Product();
        product.setFullName("fullnameeee");
        product.setId(1L);

        List<Product> productList = new LinkedList<>();
        productList.add(product);

        when(orderService.addProductToCart(1L,productList)).thenThrow(ProductAlreadyPresentInCart.class);

        // when

        mockMvc.perform(post("/orders/addProduct/1")
                .sessionAttr(TOKEN_ATTR_NAME,csrfToken)
                .sessionAttr("shoppingCart",productList)
                .param(csrfToken.getParameterName(),csrfToken.getToken()))
                    // then
                .andExpect(MockMvcResultMatchers.view().name("exception"))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

}