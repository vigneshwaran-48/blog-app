package com.vicky.blog.client;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.vicky.blog.common.exception.AppException;

import jakarta.servlet.http.HttpServletRequest;

@HttpExchange(url = "/static")
public interface StaticServiceClient {
    
    @PostExchange
    public ResponseEntity<?> addResource(@RequestParam("resource") MultipartFile resource, HttpServletRequest request,
        Principal principal) throws AppException;

    @GetExchange("/{resourceId}")
    public ResponseEntity<?> getResource(@PathVariable("resourceId") Long resourceId, HttpServletRequest request, 
        Principal principal) throws AppException;
}
