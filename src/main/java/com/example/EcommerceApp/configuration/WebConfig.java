package com.example.EcommerceApp.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/orders/cart").setViewName("shoppingCart");
        registry.addViewController("/login").setViewName("login");
    }

    // TODO: serve static from other directory
    /*
        things added to solve this
        1. YamlPropertySourceFactory bean
        2. ProjectProps
        3. value in yaml file
        4. addResourceHandlers in WebConfig bean
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.info("imagesPath: " + props.IMAGES_PATH);
//        registry.addResourceHandler("/**")
//                .addResourceLocations("file:/"+props.IMAGES_PATH+"/");
//    }

}
