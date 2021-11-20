package com.example.EcommerceApp.security.repository;

import com.example.EcommerceApp.security.email.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
