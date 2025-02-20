package com.ricardocreates.movify.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPIConfig() {
        return new OpenAPI().info(
                new Info()
                        .title("Movify")
                        .description("API to give access to a movies database")
                        .version("1.0.0")
        );
    }
}
