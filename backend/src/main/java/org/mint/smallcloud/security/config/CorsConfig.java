package org.mint.smallcloud.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    private final String FRONT_ORIGIN;
    private final String FRONT_LOCALHOST_ORIGIN;

    public CorsConfig(
        @Value("${external.origin}")
        String frontOrigin,
        @Value("${server.front-port}")
        String frontPort
    ) {
        String FRONT_LOCALHOST = "http://localhost:";
        FRONT_ORIGIN = frontOrigin;
        FRONT_LOCALHOST_ORIGIN = FRONT_LOCALHOST + frontPort;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
