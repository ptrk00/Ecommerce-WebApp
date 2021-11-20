package com.example.EcommerceApp.security.service;

import com.example.EcommerceApp.security.email.VerificationToken;
import com.example.EcommerceApp.security.exceptions.TokenDoesNotExistException;
import com.example.EcommerceApp.security.exceptions.UserFieldNotUniqueException;
import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import com.example.EcommerceApp.security.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    public User registerUser(RegistrationDetails registrationDetails) {

        User user = registrationDetails.toUser(passwordEncoder);

        if(userRepository.findByUsername(user.getUsername()) != null)
            throw new UserFieldNotUniqueException("User with provided username already exist");

        if(userRepository.findByEmail(user.getEmail()) != null)
            throw new UserFieldNotUniqueException("User with provided email already exist");

        return userRepository.save(user);
    }

    public void createVerificationToken(User user, String tokenString) {
        VerificationToken token = new VerificationToken(user,tokenString);
        verificationTokenRepository.save(token);
    }

    @Transactional
    public boolean confirmAccount(Locale locale, String token) {

        Optional<VerificationToken> verificationTokenOpt = verificationTokenRepository.findByToken(token);
        if(verificationTokenOpt.isEmpty())
            throw new TokenDoesNotExistException("Token does not exist");

        VerificationToken verificationToken = verificationTokenOpt.get();

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        // see if token has expired
        if((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0)
            return false;

        user.setEnabled(true);
        return true;
    }


}
