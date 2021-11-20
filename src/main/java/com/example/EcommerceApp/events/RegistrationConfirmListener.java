package com.example.EcommerceApp.events;

import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationConfirmListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final RegistrationService registrationService;

    private final static String MAIL_SUBJECT = "Confirm registration";

    private final JavaMailSender sender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        registrationService.createVerificationToken(user,token);

        String userEmail = user.getEmail();
        String url = event.getAppUrl() + "register/confirm?token=" + token;
        String message = "Click following link to confirm";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(MAIL_SUBJECT);
        email.setText(message + "\r\n" + "http://localhost:8080/" + url);
        sender.send(email);
    }
}
