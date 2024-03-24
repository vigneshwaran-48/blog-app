package com.vicky.blog.aspect;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vicky.blog.annotation.CommentIdValidator;
import com.vicky.blog.common.dto.comment.CommentDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.CommentService;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Aspect
@Configuration
public class CommentAspect {
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private I18NMessages i18nMessages;

    @Before("@annotation(commentIdValidator)")
    public void validateBlogId(JoinPoint joinPoint, CommentIdValidator commentIdValidator) throws AppException {
        Object[] args = joinPoint.getArgs();
        String blogId = (String) args[commentIdValidator.blogIdPosition()];
        String userId = (String) args[commentIdValidator.userIdPosition()];
        String commentId = (String) args[commentIdValidator.commentIdPosition()];

        if(userId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "User Id" }));
        }
        if(blogId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "Blog Id" }));
        }
        if(commentId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "Comment Id" }));
        }

        Optional<CommentDTO> comment = commentService.getComment(userId, blogId, commentId);
        if(comment.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NOT_EXISTS, 
                new Object[] { "Comment" }));
        }
    }
}
