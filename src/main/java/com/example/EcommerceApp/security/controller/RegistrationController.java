package com.example.EcommerceApp.security.controller;

import com.example.EcommerceApp.security.exceptions.UserFieldNotUniqueException;
import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {


    private final RegistrationService registrationService;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationDetails registrationDetails,
                                      Errors errors) {
        if(errors.hasErrors()) {
            return "/register";
        }

        registrationService.registerUser(registrationDetails);
        log.info("Registered user " + registrationDetails);
        return "redirect:/login";
    }

    @ExceptionHandler(UserFieldNotUniqueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserFieldNotUniqueException(UserFieldNotUniqueException e, Model model) {
        model.addAttribute("errorMsg", e.getMessage());
        return "exception";
    }

}
