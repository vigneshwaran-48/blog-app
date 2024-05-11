package com.vicky.blog.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/keep-alive")
public class KeepAliveController {
    
    @GetMapping
    public ResponseEntity<EmptyResponse> keepAlive(HttpServletRequest request) {
        EmptyResponse response = new EmptyResponse();
        response.setMessage("Cron service is alive!");
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }
}
