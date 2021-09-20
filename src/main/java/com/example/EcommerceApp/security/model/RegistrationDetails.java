package com.example.EcommerceApp.security.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationDetails {
    private String username;
    private String password;
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username,passwordEncoder.encode(password),email);
    }
}
