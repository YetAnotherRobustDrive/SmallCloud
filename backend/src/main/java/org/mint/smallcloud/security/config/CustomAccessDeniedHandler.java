package org.mint.smallcloud.security.config;

import org.mint.smallcloud.security.FilterExceptionManager;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private final FilterExceptionManager filterExceptionManager;

    public CustomAccessDeniedHandler(FilterExceptionManager filterExceptionManager) {
        this.filterExceptionManager = filterExceptionManager;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        filterExceptionManager.handleAuthenticationException(request, response);
    }
}
