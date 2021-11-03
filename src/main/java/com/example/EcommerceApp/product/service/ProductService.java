package com.example.EcommerceApp.product.service;

import com.example.EcommerceApp.order.ProductNotFoundException;
import com.example.EcommerceApp.product.model.CannotHandleFileException;
import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.model.UnknownFileExtensionException;
import com.example.EcommerceApp.product.repository.ProductRepository;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
@ConfigurationProperties(prefix="application")
@RequiredArgsConstructor
public class ProductService {

    @Value("${app.imagesPath}")
    private String imagesPath;

    private Integer PAGE_SIZE = 2;

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

    public Page<Product> findAllAvailableProducts(Integer page, String sortBy, Sort.Direction sort) {
        return productRepository.findByAvailableTrue(PageRequest.of(page,PAGE_SIZE,Sort.by(sort,sortBy)));
    }

    public Page<Product> findThreeNewestProducts() {
        return productRepository.findByAvailableTrue(PageRequest.of(0,3,Sort.by("postedOn")));
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

        product.setSeller(user);
        user.addOffer(product);
        return productRepository.save(product);
    }

    @Transactional
    public void markAllProductsAsUnavailable(List<Product> products) {
        products.forEach(p -> {
            p.setAvailable(Boolean.FALSE);
        });
        productRepository.saveAll(products);
    }

    public void createImgFile(Product product, MultipartFile multipartFile) {

        Optional<String> fileExtension = FileUtils.getFileExtension(multipartFile.getOriginalFilename());

        if(fileExtension.isEmpty())
            throw new UnknownFileExtensionException("No file extension");

        String ext = fileExtension.get();
        if(!(ext.equals("jpg") || ext.equals("png")))
            throw new UnknownFileExtensionException("File extension must be jpg or png");

        String productPath = product.getId() + "_" + product.getFullName();

//        String pathname = "src/main/resources/static/images/" +
//                productPath + "." + fileExtension.get();

        String pathname = imagesPath +
                productPath + "." + fileExtension.get();

        product.setImagePath(productPath + "." + fileExtension.get());
        productRepository.save(product);
        File file = new File(pathname);

        try (InputStream is = multipartFile.getInputStream()) {
            Files.copy(is,file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CannotHandleFileException("Could not create file with original name: "
                    + multipartFile.getOriginalFilename());
        }

    }

    public List<Product> findByUser(User user) {
        return productRepository.findBySeller(user);
    }
}
