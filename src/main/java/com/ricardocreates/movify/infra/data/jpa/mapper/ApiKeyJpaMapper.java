package com.ricardocreates.movify.infra.data.jpa.mapper;

import com.ricardocreates.movify.domain.entity.ApiKey;
import com.ricardocreates.movify.infra.data.jpa.model.ApiKeyEntity;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ApiKeyJpaMapper {
    ApiKey toDomain(ApiKeyEntity apiKeyEntity);

    default Optional<ApiKey> toDomain(Optional<ApiKeyEntity> apiKeyEntity) {
        return apiKeyEntity.map(this::toDomain);
    }
}
