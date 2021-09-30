package com.example.EcommerceApp.security.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class RegistrationDetails {

    @Size(min=5)
    private String username;

    @Size(min=5)
    private String password;

    @Email
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username,passwordEncoder.encode(password),email);
    }
}
