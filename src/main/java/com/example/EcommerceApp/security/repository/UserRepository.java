package com.example.EcommerceApp.security.repository;

import com.example.EcommerceApp.security.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
