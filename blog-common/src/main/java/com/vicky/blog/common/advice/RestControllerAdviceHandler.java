package com.vicky.blog.common.advice;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(DataIntegrityViolationException ex, HttpServletRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        AppErrorResponse response = new AppErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "DB operation failure", LocalDateTime.now(), request.getServletPath());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        AppErrorResponse response = new AppErrorResponse(HttpStatus.BAD_REQUEST.value(), "Upload size is larger", 
                LocalDateTime.now(), request.getServletPath());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleException(WebClientResponseException ex) {
        LOGGER.error(ex.getMessage(), ex);
        AppErrorResponse response = ex.getResponseBodyAs(AppErrorResponse.class);
        return ResponseEntity.status(response != null ? response.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .body(response);
    }
}
