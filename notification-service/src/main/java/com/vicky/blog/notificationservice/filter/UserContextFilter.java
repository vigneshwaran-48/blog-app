package com.vicky.blog.notificationservice.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.vicky.blog.notificationservice.util.UserContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String bearerToken = httpRequest.getHeader("Authorization");
        String accessToken = null;
        if(bearerToken != null && bearerToken.split(" ").length == 2) {
            accessToken = bearerToken.split(" ")[1];
        }
        UserContextHolder.getContext().setAccessToken(accessToken);
        
        chain.doFilter(request, response);

        UserContextHolder.clearContext();
    }

}
