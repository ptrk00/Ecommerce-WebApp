package com.example.EcommerceApp.security.controller;

import com.example.EcommerceApp.events.OnRegistrationCompleteEvent;
import com.example.EcommerceApp.events.RegistrationConfirmListener;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.security.exceptions.UserFieldNotUniqueException;
import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.repository.UserRepository;
import com.example.EcommerceApp.security.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegistrationService registrationService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ApplicationEventPublisher eventPublisher;


    // TODO: fix
    @Disabled
    @Test
    public void should_register_user() throws Exception {

        // given

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setUsername("user1");
        registrationDetails.setPassword("passwd");
        registrationDetails.setEmail("email@email.email");

        // when

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username",registrationDetails.getUsername())
                .param("password",registrationDetails.getPassword())
                .param("email",registrationDetails.getEmail())
        .with(csrf()))

                // then

                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));

        verify(registrationService, times(1)).registerUser(registrationDetails);

    }

    @Test
    public void should_catch_UserFieldNotUniqueException() throws Exception {

        // given

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setUsername("user1");
        registrationDetails.setPassword("passwd");
        registrationDetails.setEmail("email@email.email");

        when(registrationService.registerUser(registrationDetails)).thenThrow(UserFieldNotUniqueException.class);

        // when

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username",registrationDetails.getUsername())
                .param("password",registrationDetails.getPassword())
                .param("email",registrationDetails.getEmail())
                .with(csrf()))

                // then

                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.view().name("exception"));

    }

}