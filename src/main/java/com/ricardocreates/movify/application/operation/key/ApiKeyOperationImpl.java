package com.ricardocreates.movify.application.operation.key;

import com.ricardocreates.movify.domain.entity.ApiKey;
import com.ricardocreates.movify.domain.operation.ApiKeyOperation;
import com.ricardocreates.movify.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyOperationImpl implements ApiKeyOperation {
    private final ApiKeyRepository apiKeyRepository;

    @Override
    @Cacheable(
            value = "findKeyByKeyValue",
            key = "#key")
    public ApiKey findByKey(String key) {
        return apiKeyRepository.findByKey(key).orElse(null);
    }
}
