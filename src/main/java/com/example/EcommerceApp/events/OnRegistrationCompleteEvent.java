package com.example.EcommerceApp.events;

import com.example.EcommerceApp.security.model.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
    }
}
