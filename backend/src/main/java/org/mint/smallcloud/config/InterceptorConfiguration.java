package org.mint.smallcloud.config;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.log.user.UserLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final UserLogInterceptor userLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLogInterceptor)
            .addPathPatterns("/**");
    }
}
