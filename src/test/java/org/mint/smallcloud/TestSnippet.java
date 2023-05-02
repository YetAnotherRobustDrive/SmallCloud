package org.mint.smallcloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class TestSnippet {
    public static MockHttpServletRequestBuilder post(String url, ObjectMapper objectMapper, Object dto) throws JsonProcessingException {
        return RestDocumentationRequestBuilders.post(url)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}
