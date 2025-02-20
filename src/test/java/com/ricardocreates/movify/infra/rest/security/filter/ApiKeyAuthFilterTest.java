package com.ricardocreates.movify.infra.rest.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricardocreates.movify.domain.entity.ApiKey;
import com.ricardocreates.movify.domain.operation.ApiKeyOperation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ApiKeyAuthFilterTest {

    @Mock
    private ApiKeyOperation apiKeyOperation;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private ApiKeyAuthFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilter_WithValidApiKey() throws Exception {
        given(request.getRequestURI()).willReturn("/api/test");
        given(request.getParameter("api_key")).willReturn("valid_key");
        given(apiKeyOperation.findByKey("valid_key")).willReturn(new ApiKey());

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void testDoFilter_WithoutApiKey() throws Exception {
        given(request.getRequestURI()).willReturn("/api/test");
        given(request.getParameter("api_key")).willReturn(null);
        given(response.getWriter()).willReturn(writer);

        filter.doFilter(request, response, chain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    void testDoFilter_WithInvalidApiKey() throws Exception {
        given(request.getRequestURI()).willReturn("/api/test");
        given(request.getParameter("api_key")).willReturn("invalid_key");
        given(apiKeyOperation.findByKey("invalid_key")).willReturn(null);
        given(response.getWriter()).willReturn(writer);

        filter.doFilter(request, response, chain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    void testDoFilter_NonApiRequest() throws Exception {
        given(request.getRequestURI()).willReturn("/public/home");

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
