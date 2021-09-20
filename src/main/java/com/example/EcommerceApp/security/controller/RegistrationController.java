package com.example.EcommerceApp.security.controller;

import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String processRegistration(RegistrationDetails registrationDetails) {
        userRepository.save(registrationDetails.toUser(passwordEncoder));
        log.info("Registered user " + registrationDetails);
        return "redirect:/login";
    }


}
