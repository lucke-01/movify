package com.ricardocreates.movify.infra.rest.config.validation.converter;

import com.swagger.client.codegen.rest.model.OrderTypeDTO;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

public class OrderTypeConverter implements Converter<String, OrderTypeDTO> {
    @Override
    public OrderTypeDTO convert(@Nonnull String source) {
        return OrderTypeDTO.fromValue(source);
    }
}
