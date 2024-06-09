package com.vicky.blog.common.service;

import com.vicky.blog.common.exception.AppException;

public interface EmailService {
    
    void sendEmail(String to, String subject, String content) throws AppException;
}
