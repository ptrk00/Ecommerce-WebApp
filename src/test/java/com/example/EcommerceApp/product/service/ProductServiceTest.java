package com.example.EcommerceApp.product.service;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.model.SellerDetails;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.security.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void should_throw_when_given_invalid_id() {

        // given

        Long badId = 5L;

        when(productRepository.findById(badId)).thenReturn(Optional.empty());

        // when + then

        assertThatThrownBy(() -> {
            productService.findProduct(badId);
        }).isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(Long.toString(badId));
    }

    @Test
    void should_return_product_when_given_valid_id() {

        // given

        Long id = 5L;
        String productName = "Product's full name";
        Product product = new Product();
        product.setFullName(productName);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // when

        Product returned = productService.findProduct(id);

        // then

        assertThat(returned.getFullName()).isEqualTo(productName);

    }

    @Test
    void should_not_throw_when_given_valid_id() {

        // given

        Long id = 5L;
        String productName = "Product's full name";
        Product product = new Product();
        product.setFullName(productName);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // when

        Throwable throwable = catchThrowable(() -> productService.findProduct(id));

        // then

        assertThat(throwable).isNull();

    }

    @Test
    public void should_register_product() {

        // given

        Long id = 5L;
        String productName = "Product's full name";
        Product product = new Product();
        product.setFullName(productName);

        User user = new User("user1","12345","em@em.em");

        // when

        productService.registerProduct(product,user);

        // then

        assertThat(user.getOffers()).isNotNull();
        assertThat(product.getSeller()).isEqualTo(user);

        verify(productRepository, times(1)).save(product);

    }


    // TODO: finish

    @Test
    public void should_register_rating() {

        // given
        ProductRating productRating = new ProductRating(null,5,"Super","Autor",new Date());
        User user = new User("usrnm","passwd","em@em.em");
        Long productId = 5L;

        Product product = new Product();
        product.setId(1L);
        product.setFullName("product1");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));


        // then
        productService.registerRating(productId,productRating,user);

        assertThat(productRating.getProduct()).isEqualTo(product);
        assertThat(productRating.getAuthor()).isEqualTo(user.getUsername());
        assertThat(productRating).isIn(product.getRatings());

    }


    // TODO: write test
    @Test
    @Disabled
    public void should_generate_file() throws IOException {

        // given

        Product product = new Product();
        product.setId(1L);
        product.setFullName("product1");

        byte[] buff = {0,1,2,3};

        MultipartFile mock = Mockito.mock(MultipartFile.class);
        when(mock.getOriginalFilename()).thenReturn("somerandomname.jpg");


        // dont allow to actually create a file
        when(mock.getInputStream()).thenThrow(new IOException());


        // when

        productService.createImgFile(product,mock);

        // then


    }

}