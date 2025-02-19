package com.ricardocreates.movify.operation.key;

import com.ricardocreates.movify.application.operation.key.ApiKeyOperationImpl;
import com.ricardocreates.movify.domain.entity.ApiKey;
import com.ricardocreates.movify.domain.repository.ApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApiKeyOperationImplTest {
    @Mock
    private ApiKeyRepository apiKeyRepository;
    @InjectMocks
    private ApiKeyOperationImpl apiKeyOperationImpl;

    @Test
    void should_findKey() {
        //GIVEN
        String key = "1234";
        ApiKey apiKey = new ApiKey(1L, key);
        given(apiKeyRepository.findByKey(any(String.class)))
                .willReturn(Optional.of(apiKey));
        //WHEN
        ApiKey apiKeyResult = apiKeyOperationImpl.findByKey(key);
        //THEN
        assertThat(apiKeyResult).isNotNull();
        assertThat(apiKeyResult.getKeyValue()).isEqualTo(key);
    }

    @Test
    void should_not_findKey() {
        //GIVEN
        String key = "1234";
        given(apiKeyRepository.findByKey(any(String.class)))
                .willReturn(Optional.empty());
        //WHEN
        ApiKey apiKeyResult = apiKeyOperationImpl.findByKey(key);
        //THEN
        assertThat(apiKeyResult).isNull();
    }
}
