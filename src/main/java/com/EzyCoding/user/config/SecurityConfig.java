package com.EzyCoding.user.config;

import com.EzyCoding.user.handler.CustomOAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomOAuth2SuccessHandler successHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        OpaqueTokenIntrospector googleOpaqueTokenIntrospector) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll())
                .oauth2Login(oauth -> oauth.successHandler(successHandler))
                .oauth2ResourceServer(auth -> auth
                        .opaqueToken(opaque -> opaque.introspector(googleOpaqueTokenIntrospector)));
        return http.build() ;
    }
}
