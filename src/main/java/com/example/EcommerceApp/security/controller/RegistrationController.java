package com.example.EcommerceApp.security.controller;

import com.example.EcommerceApp.events.OnRegistrationCompleteEvent;
import com.example.EcommerceApp.security.email.VerificationToken;
import com.example.EcommerceApp.security.exceptions.TokenDoesNotExistException;
import com.example.EcommerceApp.security.exceptions.UserFieldNotUniqueException;
import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.VerificationTokenRepository;
import com.example.EcommerceApp.security.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/register")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {


    private final RegistrationService registrationService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationDetails registrationDetails,
                                      Errors errors, HttpServletRequest request) {
        if(errors.hasErrors()) {
            return "/register";
        }

        User user = registrationService.registerUser(registrationDetails);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(),
                request.getContextPath()));
        log.info("Registered user " + registrationDetails);
        return "redirect:/login";
    }

    @GetMapping("/confirm")
    public String confirmUser(WebRequest request, Model model,
                              @RequestParam("token") String token) {

        Locale locale = request.getLocale();
        boolean registeredSuccessfully = registrationService.confirmAccount(locale,token);

        // token has expired
        // TODO: finish
        if(!registeredSuccessfully)
            return "redirect:/exception.html";
        else
            return "redirect:/login";
    }

    @ExceptionHandler(TokenDoesNotExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserTokenDoesNotExistException(TokenDoesNotExistException e, Model model) {
        model.addAttribute("errorMsg", e.getMessage());
        return "exception";
    }

    @ExceptionHandler(UserFieldNotUniqueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserFieldNotUniqueException(UserFieldNotUniqueException e, Model model) {
        model.addAttribute("errorMsg", e.getMessage());
        return "exception";
    }

}
