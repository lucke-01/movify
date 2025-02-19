package com.ricardocreates.movify.infra.rest.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricardocreates.movify.domain.entity.ApiKey;
import com.ricardocreates.movify.domain.operation.ApiKeyOperation;
import com.swagger.client.codegen.rest.model.GenericErrorResponseDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(1)
public class ApiKeyAuthFilter implements Filter {

    private static final String API_KEY_QUERY_PARAMETER = "api_key";

    private final ApiKeyOperation apiKeyOperation;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getRequestURI().contains("/api/")) {
            String apiKey = httpRequest.getParameter(API_KEY_QUERY_PARAMETER);
            if (apiKey == null || apiKey.isEmpty()) {
                this.writeUnauthorizedResponse(response);
                return;
            }
            ApiKey apiKeyFound = apiKeyOperation.findByKey(apiKey);
            if (apiKeyFound == null) {
                this.writeUnauthorizedResponse(response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void writeUnauthorizedResponse(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        GenericErrorResponseDTO genericResponse = new GenericErrorResponseDTO();
        genericResponse.setErrorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        genericResponse.setMessage("Valid API key should be provided");
        genericResponse.setDescription("Contact with administrator to set your key");
        String genericResponseString = objectMapper.writeValueAsString(genericResponse);
        response.getWriter().write(genericResponseString);
        response.getWriter().flush();
    }
}
