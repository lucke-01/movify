package com.ricardocreates.movify.infra.rest.config;

import com.ricardocreates.movify.infra.rest.config.validation.converter.OrderByConverter;
import com.ricardocreates.movify.infra.rest.config.validation.converter.OrderTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OrderByConverter());
        registry.addConverter(new OrderTypeConverter());
    }
}