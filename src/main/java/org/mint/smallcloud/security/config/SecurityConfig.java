package org.mint.smallcloud.security.config;

import lombok.RequiredArgsConstructor;
import org.mint.smallcloud.security.FilterExceptionManager;
import org.mint.smallcloud.security.UserDetailsProvider;
import org.mint.smallcloud.security.jwt.JwtFilter;
import org.mint.smallcloud.security.jwt.JwtTokenProvider;
import org.mint.smallcloud.user.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsProvider userDetailsProvider;
    private final FilterExceptionManager filterExceptionManager;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final String[] PERMIT_ALL = {
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/docs"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and().httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/admin").hasRole(Roles.ADMIN.getRole())
                .antMatchers("/auth/user").hasRole(Roles.COMMON.getRole())
                .antMatchers(PERMIT_ALL).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider, userDetailsProvider, filterExceptionManager), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        return http.build();
    }


}
