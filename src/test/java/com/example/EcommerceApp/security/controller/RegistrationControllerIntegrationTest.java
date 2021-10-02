package com.example.EcommerceApp.security.controller;

import com.example.EcommerceApp.security.model.RegistrationDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_register_user_when_valid_data() throws Exception {

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

    }

    @Test
    public void should_validate_registration_data() throws Exception {

        // given

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setUsername("user1");

        // to short password
        registrationDetails.setPassword("123");

        registrationDetails.setEmail("email@email.email");

        // when

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username",registrationDetails.getUsername())
                .param("password",registrationDetails.getPassword())
                .param("email",registrationDetails.getEmail())
                .with(csrf()))

                // then

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.view().name("/register"));

    }

}
