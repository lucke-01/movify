package com.ricardocreates.movify.infra.jpa;

import com.ricardocreates.movify.application.operation.key.ApiKeyOperationImpl;
import com.ricardocreates.movify.domain.entity.ApiKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test"})
public class JpaApiKeyRepositoryImplIntegrationTest {
    @Autowired
    private ApiKeyOperationImpl apiKeyOperationImpl;

    @Test
    void should_found_key() {
        //GIVEN
        String key = "1234";
        //WHEN
        ApiKey apiKeyFound = apiKeyOperationImpl.findByKey(key);
        //THEN
        assertThat(apiKeyFound).isNotNull();
        assertThat(apiKeyFound.getKeyValue()).isEqualTo(key);
    }

    @Test
    void should_not_found_key() {
        //GIVEN
        String key = "12";
        //WHEN
        ApiKey apiKeyFound = apiKeyOperationImpl.findByKey(key);
        //THEN
        assertThat(apiKeyFound).isNull();
    }
}
