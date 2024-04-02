package com.vicky.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf().disable()
                    .authorizeHttpRequests(http -> {
                        http
                            .requestMatchers("/api/v1/app/blog/feeds")
                                .permitAll()
                            .anyRequest()
                                .authenticated();
                    })
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                    .build();
    }

    @Bean
	JwtDecoder jwtDecoder() {
	    return JwtDecoders.fromIssuerLocation("https://accounts.google.com");
	}
    
}
