package com.vicky.blog.aspect;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Aspect
@Configuration
public class UserAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private I18NMessages i18nMessages;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAspect.class);

    @Before("@annotation(userIdValidator)")
    public void userIdValidator(JoinPoint joinPoint, UserIdValidator userIdValidator) throws Throwable {

        int[] positionsToCheck = userIdValidator.positions();

        Object[] args = joinPoint.getArgs();

        for(int position : positionsToCheck) {
            String id = (String) args[position];

            if(id == null) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "User Id" }));
            }

            Optional<UserDTO> user = userService.getUser(id);
            if(user.isEmpty()) {
                LOGGER.error("User {} not exists", id);
                throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NOT_EXISTS, 
                    new Object[] { "User" }));
            }
        }
    }
}
