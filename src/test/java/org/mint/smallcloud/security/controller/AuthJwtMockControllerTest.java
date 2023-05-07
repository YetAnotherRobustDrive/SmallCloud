package org.mint.smallcloud.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mint.smallcloud.TestSnippet;
import org.mint.smallcloud.security.jwt.JwtTokenProvider;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class AuthJwtMockControllerTest {
    private MockMvc mockMvc;
    private final String URL_PREFIX = "/auth";

    @MockBean
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .build();
    }

    @Test
    @DisplayName("/auth/refresh document")
    public void refresh() throws Exception {
        final String url = URL_PREFIX + "/refresh";
        String refresh = "refresh";
        String success = "jwtSuccessToken";

        /* token provider mocking */
        when(tokenProvider.generateAccessTokenFromRefreshToken(eq(refresh), any()))
            .thenReturn(success);
        doNothing().when(tokenProvider).validateToken(refresh);
        when(tokenProvider.resolveTokenFromHeader(any()))
            .thenCallRealMethod();
        /* token provider mocking */

        this.mockMvc.perform(TestSnippet.secureGet(url, refresh))
            .andExpect(status().isOk())
            .andExpect(content().string(success))
            .andDo(document("Refresh"));

        doCallRealMethod().when(tokenProvider).validateToken(any());
        this.mockMvc.perform(TestSnippet.secureGet(url, "abc"))
            .andExpect(status().isBadRequest())
            .andDo(document("RefreshBadToken"));
    }
}
