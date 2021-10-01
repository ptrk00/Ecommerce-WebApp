package com.example.EcommerceApp.order;

import com.example.EcommerceApp.security.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<ProductOrder,Long> {
    List<ProductOrder> findByUser(User user);
}
