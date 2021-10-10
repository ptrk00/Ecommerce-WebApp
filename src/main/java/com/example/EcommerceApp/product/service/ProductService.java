package com.example.EcommerceApp.product.service;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;


@Service
//@ConfigurationProperties(prefix="ecommerce.static")
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Iterable<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if(optProduct.isEmpty())
            throw new ProductNotFoundException("Product with id " + id + " has not been found");
        return optProduct.get();
    }

    @Transactional
    public Product registerRating(Long productId, ProductRating productRating, User user) {
        // id is somehow not null, set it to null
        productRating.setId(null);
        Product product = findProduct(productId);
        productRating.setProduct(product);
        productRating.setAuthor(user.getUsername());
        product.addRating(productRating);
        return product;
    }

    @Transactional
    public Product registerProduct(Product product, User user) {

        // TODO: use @PostPersist to set image path <- not working
        product.setSeller(user);
        user.addOffer(product);
        return productRepository.save(product);
    }

    public void createImgFile(Product product, MultipartFile multipartFile) {

        Optional<String> fileExtension = FileUtils.getFileExtension(multipartFile.getOriginalFilename());

        // TODO: handle exception

        if(fileExtension.isEmpty())
            throw new RuntimeException("No file extension");

//        String pathname = "src/main/resources/static/images/" +
//                product.getImagePath() + "." + fileExtension.get();

        String productPath = product.getId() + "_" + product.getFullName();
//        String pathname = projectProps.IMAGES_PATH + "\\"+
//                productPath + "." + fileExtension.get();

        String pathname = "src/main/resources/static/images/" +
                productPath + "." + fileExtension.get();

        product.setImagePath(productPath + "." + fileExtension.get());
        productRepository.save(product);
        File file = new File(pathname);

        // TODO: handle exception

        try (InputStream is = multipartFile.getInputStream()) {
            Files.copy(is,file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Product> findByUser(User user) {
        return productRepository.findBySeller(user);
    }
}
