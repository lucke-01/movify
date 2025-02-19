package com.ricardocreates.movify.infra.data.jpa.repository;

import com.ricardocreates.movify.infra.data.jpa.model.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    Optional<ApiKeyEntity> findByKeyValue(String key);
}
