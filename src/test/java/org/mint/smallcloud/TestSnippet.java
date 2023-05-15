package org.mint.smallcloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class TestSnippet {
    private final static String AUTH_NAME = "Authorization";
    private final static String AUTH_VALUE_PREFIX = "Bearer ";

    public static MockHttpServletRequestBuilder post(String url, ObjectMapper objectMapper, Object dto) throws JsonProcessingException {
        return RestDocumentationRequestBuilders.post(url)
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder securePost(String url, String token, ObjectMapper objectMapper, Object dto) throws JsonProcessingException {
        return RestDocumentationRequestBuilders.post(url)
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTH_NAME, AUTH_VALUE_PREFIX + token)
            .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder securePost(String url, String token) throws JsonProcessingException {
        return RestDocumentationRequestBuilders.post(url)
            .header(AUTH_NAME, AUTH_VALUE_PREFIX + token)
            .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder secureGet(String url, String token) {
        return RestDocumentationRequestBuilders.get(url)
            .header(AUTH_NAME, AUTH_VALUE_PREFIX + token);
    }

    public static MockHttpServletRequestBuilder secured(MockHttpServletRequestBuilder request, String token) throws JsonProcessingException {
        return request
            .header(AUTH_NAME, AUTH_VALUE_PREFIX + token)
            .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder secured(MockHttpServletRequestBuilder request, String token, ObjectMapper objectMapper, Object dto) throws JsonProcessingException {
        return request
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTH_NAME, AUTH_VALUE_PREFIX + token)
            .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder withSecurePost(String url, String userId, ObjectMapper objectMapper, Object dto, String... roles) throws JsonProcessingException {
        return RestDocumentationRequestBuilders.post(url)
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.user(userId).roles(roles));
    }
}
