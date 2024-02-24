package com.vicky.blog.aspect;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Aspect
@Configuration
public class BlogAspect {

    @Autowired
    private BlogService blogService;

    @Autowired
    private I18NMessages i18nMessages;
    
    @Before("@annotation(blogIdValidator)")
    public void validateBlogId(JoinPoint joinPoint, BlogIdValidator blogIdValidator) throws AppException {

        Object[] args = joinPoint.getArgs();
        Long blogId = (Long) args[blogIdValidator.blogIdPosition()];
        String userId = (String) args[blogIdValidator.userIdPosition()];

        if(userId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "User Id" }));
        }
        if(blogId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "Blog Id" }));
        }

        Optional<BlogDTO> blog = blogService.getBlog(userId, blogId);
        if(blog.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NOT_EXISTS, 
                new Object[] { "Blog" }));
        }
    }
}
