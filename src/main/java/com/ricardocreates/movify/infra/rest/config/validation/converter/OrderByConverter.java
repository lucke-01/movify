package com.ricardocreates.movify.infra.rest.config.validation.converter;

import com.swagger.client.codegen.rest.model.OrderByDTO;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

public class OrderByConverter implements Converter<String, OrderByDTO> {
    @Override
    public OrderByDTO convert(@Nonnull String source) {
        return OrderByDTO.fromValue(source);
    }
}
