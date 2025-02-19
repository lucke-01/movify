package com.ricardocreates.movify.domain.repository;

import com.ricardocreates.movify.domain.entity.ApiKey;

import java.util.Optional;

public interface ApiKeyRepository {
    Optional<ApiKey> findByKey(String keyValue);
}
