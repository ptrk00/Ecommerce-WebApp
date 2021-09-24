package com.example.EcommerceApp.security.service;

import com.example.EcommerceApp.security.exceptions.UserFieldNotUniqueException;
import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegistrationDetails registrationDetails) {

        User user = registrationDetails.toUser(passwordEncoder);

        if(userRepository.findByUsername(user.getUsername()) != null)
            throw new UserFieldNotUniqueException("User with provided username already exist");

        if(userRepository.findByEmail(user.getEmail()) != null)
            throw new UserFieldNotUniqueException("User with provided email already exist");

        return userRepository.save(user);
    }

}
