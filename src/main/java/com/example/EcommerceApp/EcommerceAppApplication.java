package com.example.EcommerceApp;

import com.example.EcommerceApp.product.model.Product;
import com.example.EcommerceApp.product.model.ProductAttribute;
import com.example.EcommerceApp.product.model.ProductRating;
import com.example.EcommerceApp.product.model.SellerDetails;
import com.example.EcommerceApp.product.repository.ProductAttributeRepository;
import com.example.EcommerceApp.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class EcommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(ProductRepository productRepository) {

		Product product = new Product("Gaming Laptop",
				2300.0,
				new SellerDetails("Gaming Laptop Company",
						"Poland",
						"Cn-121212"),
				"Elegant and powerful gaming laptop. Has very cool" +
						"screen.",
				"long description");

		List<ProductRating> productRatingList = List.of(new ProductRating(product,
				4,
				"Cudowny jest bardzo. Polecam cieplutko",
				"Tajemniczy Kupiec",
				new Date()),
				new ProductRating(product,1,"Syf starszny. Szkoada gadać!","X", new Date()));

		List<ProductAttribute> productAttributes = List.of(new ProductAttribute(product,"Stan","Nowy"));

		product.setRatings(productRatingList);
		product.setProductAttributes(productAttributes);

		List<Product> productList = List.of(new Product("Headphones Super-Photo 2980",
				1100.0,
				new SellerDetails("Some Illegal Company",
						"USA",
						"Cn-121212"),
				"Headphones with really loud bass. Your " +
						" neighbours can listen with you ",
				"long description"),
				new Product("Random ancient statue",
						9990.0,
						new SellerDetails("DefinitelyNoScam",
								"Germany",
								"Cn-121212"),
						"I have just found it in my garden lol, but" +
								"if definitely belongs to some ancient greeks or something",
						"long description"),
				new Product("A math book ",
						50.0,
						new SellerDetails("Math big brainzz 2012 IV edition",
								"France",
								"Cn-121212"),
						"Learn algebra, calculus and some" +
								"other strange stuff",
						"long description"),
				new Product("Sport shoos. Good for jumping",
						230.0,
						new SellerDetails("BootsAndGoods",
								"Sweden",
								"Cn-121212"),
						"You can not only walk in those" +
								"boots bu also you can jump! That's unbelievable!",
						"long description"));

		return args -> {
			Product p = productRepository.save(product);
			productRepository.saveAll(productList);
		};

	}

}
