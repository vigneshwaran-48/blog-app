package com.vicky.blog.staticservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${oauth2.server.url}")
    private String oauth2ServerURL;

    private static final Long MAX_AGE = 3600L;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf().disable()
                    .authorizeHttpRequests(http -> {
                        http
                            .requestMatchers(HttpMethod.GET, "/static/**")
                                .permitAll()
                            .anyRequest().authenticated();
                    })
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                    .build();
    }

    @Bean
	JwtDecoder jwtDecoder() {
	    return JwtDecoders.fromIssuerLocation("https://accounts.google.com");
	}
    
}
