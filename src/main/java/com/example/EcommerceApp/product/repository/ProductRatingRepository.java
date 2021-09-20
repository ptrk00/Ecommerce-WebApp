package com.example.EcommerceApp.product.repository;

import com.example.EcommerceApp.product.model.ProductRating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRatingRepository extends CrudRepository<ProductRating,Long> {
}
