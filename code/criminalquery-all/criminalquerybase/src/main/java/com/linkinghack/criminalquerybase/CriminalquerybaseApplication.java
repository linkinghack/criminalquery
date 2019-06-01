package com.linkinghack.criminalquerybase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CriminalquerybaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CriminalquerybaseApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("POST","PUT","PATCH","DELETE","HEAD","GET")
                        .allowCredentials(true)
                        .allowedOrigins("https://tyut.life","http://tyut.life", "https://grad.linkinghack.com");
            }
        };
    }

}
