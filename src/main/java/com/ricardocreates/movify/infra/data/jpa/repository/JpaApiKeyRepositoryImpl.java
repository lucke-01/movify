package com.ricardocreates.movify.infra.data.jpa.repository;

import com.ricardocreates.movify.domain.entity.ApiKey;
import com.ricardocreates.movify.domain.repository.ApiKeyRepository;
import com.ricardocreates.movify.infra.data.jpa.mapper.ApiKeyJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaApiKeyRepositoryImpl implements ApiKeyRepository {
    private final ApiKeyJpaMapper apiKeyJpaMapper;
    private final DataApiKeyRepository dataApiKeyRepository;

    @Override
    public Optional<ApiKey> findByKey(String key) {
        return apiKeyJpaMapper.toDomain(dataApiKeyRepository
                .findByKeyValue(key));
    }
}