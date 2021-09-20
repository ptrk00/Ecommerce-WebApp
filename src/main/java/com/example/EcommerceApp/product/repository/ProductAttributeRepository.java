package com.example.EcommerceApp.product.repository;

import com.example.EcommerceApp.product.model.ProductAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends CrudRepository<ProductAttribute,Long> {
}
