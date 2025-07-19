package com.jobdam.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // <-- more inclusive than "/*"
                .allowedOrigins(
                        "http://localhost:5173",        // for local dev
                        "https://jobdams.online"        // for production
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")                  // allow all headers
                .allowCredentials(true);
    }
}