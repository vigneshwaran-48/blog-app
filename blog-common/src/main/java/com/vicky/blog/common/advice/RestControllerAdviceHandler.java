package com.vicky.blog.common.advice;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vicky.blog.common.dto.AppErrorResponse;
import com.vicky.blog.common.exception.AppException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestControllerAdviceHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(RestControllerAdviceHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        AppErrorResponse response = new AppErrorResponse(500, ex.getMessage(), 
                                                        LocalDateTime.now(), request.getServletPath());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleException(AppException ex, HttpServletRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        AppErrorResponse response = new AppErrorResponse(ex.getStatus(), ex.getMessage(), 
                                                        LocalDateTime.now(), request.getServletPath());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
