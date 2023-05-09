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
    private final String LOCAL_ORIGIN;

    public CorsConfig(
        @Value("${external.front_origin}")
        String frontOrigin,
        @Value("${external.local_origin}")
        String localOrigin) {
        FRONT_ORIGIN = frontOrigin;
        LOCAL_ORIGIN = localOrigin;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(FRONT_ORIGIN);
        config.addAllowedOrigin(LOCAL_ORIGIN);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
