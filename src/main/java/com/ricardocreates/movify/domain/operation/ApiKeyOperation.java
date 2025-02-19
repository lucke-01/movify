package com.ricardocreates.movify.domain.operation;

import com.ricardocreates.movify.domain.entity.ApiKey;

public interface ApiKeyOperation {
    ApiKey findByKey(String key);
}
