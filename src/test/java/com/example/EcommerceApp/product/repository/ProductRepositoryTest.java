package com.example.EcommerceApp.product.repository;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.model.ProducerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

//    @BeforeEach
//    void loadData() {
//
//
//    }

    @Test
    void shouldRetrieveProductAndItsRelations() {

        // given


        Product product = new Product("fullName:))))))",
                230.0,
                new ProducerDetails("Polska","Kraków"),
                "short description",
                "long description");

        List<ProductRating> productRatingList = List.of(new ProductRating(product,
                        4,
                        "Cudowny jest bardzo. Polecam cieplutko",
                        "Tajemniczy Kupiec",
                        new Date()),
                new ProductRating(product,1,"Syf starszny. Szkoada gadać!","X", new Date()));

       // List<ProductAttribute> productAttributes = List.of(new ProductAttribute(product,"Stan","Nowy"));

        product.setRatings(productRatingList);
       // product.setProductAttributes(productAttributes);

        product = productRepository.save(product);

        // when

        Optional<Product> retrieved = productRepository.findById(product.getId());

        // then

        assertThat(retrieved.get()).isEqualTo(product);

        // make sure that product's relations are loaded from db correctly

        assertThat(retrieved.get().getRatings().get(1).getDescription()).isEqualTo("Syf starszny. Szkoada gadać!");
      //  assertThat(retrieved.get().getProductAttributes().get(0).getAttributeName()).isEqualTo("Stan");
    }

}