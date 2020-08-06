package com.sessionknip.socialnet.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

public class CorsConfig {

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.addAllowedOrigin("http://192.168.1.59:8080");
//        configuration.addAllowedOrigin("http://localhost:3000");
//        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Accept", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//        configuration.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", configuration);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//
//        return bean;
//    }


}
