package org.mint.smallcloud.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mint.smallcloud.security.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class) // When using JUnit5
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/auth";
    private final String REGISTER_URL = URL_PREFIX + "/register";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void register() throws Exception {
        RegisterDto registerDto =
            RegisterDto.builder()
                    .id("test1")
                    .name("testName")
                    .password("pw").build();
        this.mockMvc.perform(
                post(REGISTER_URL)
                        .content(objectMapper.writeValueAsString(registerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("Register",
                        requestFields(
                                fieldWithPath("id").description("user id"),
                                fieldWithPath("name").description("user nickname"),
                                fieldWithPath("password").description("user password"))));

        this.mockMvc.perform(
                        post(REGISTER_URL)
                                .content(objectMapper.writeValueAsString(registerDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(document("RegisterFail",
                       requestFields(
                                fieldWithPath("id").description("user id"),
                                fieldWithPath("name").description("user nickname"),
                                fieldWithPath("password").description("user password"))));
    }
    
}