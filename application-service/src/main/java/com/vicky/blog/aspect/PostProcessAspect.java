package com.vicky.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import com.vicky.blog.annotation.PostProcess;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.utility.PostProcessType;

@Aspect
@Configuration
public class PostProcessAspect {
    
    @Before("@annotation(postProcess)")
    public void validateBlogId(JoinPoint joinPoint, PostProcess postProcess) throws AppException {

        PostProcessType type = postProcess.type();
        switch (type) {
            case PUBLISH_BLOG:
                
                break;
        
            default:
                break;
        }
        
    }
}
