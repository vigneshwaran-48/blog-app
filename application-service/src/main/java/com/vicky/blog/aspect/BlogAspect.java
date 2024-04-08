package com.vicky.blog.aspect;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.vicky.blog.annotation.BlogAccessTracker;
import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogFeedsDTO.PageStatus;
import com.vicky.blog.common.dto.redis.UserAccessDetails;
import com.vicky.blog.common.dto.user.UserDTO.UserType;
import com.vicky.blog.common.exception.AccessLimitReachedException;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.config.RedisConfiguration;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Aspect
@Configuration
public class BlogAspect {

    @Autowired
    private BlogService blogService;

    @Autowired
    private I18NMessages i18nMessages;

    @Autowired
    private RedisTemplate<String, UserAccessDetails> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisConfiguration redisConfiguration;

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogAspect.class);

    @Before("@annotation(blogIdValidator)")
    public void validateBlogId(JoinPoint joinPoint, BlogIdValidator blogIdValidator) throws AppException {

        Object[] args = joinPoint.getArgs();
        String blogId = (String) args[blogIdValidator.blogIdPosition()];
        String userId = (String) args[blogIdValidator.userIdPosition()];

        if (userId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST,
                    i18nMessages.getMessage(I18NMessage.REQUIRED, new Object[] { "User Id" }));
        }
        if (blogId == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST,
                    i18nMessages.getMessage(I18NMessage.REQUIRED, new Object[] { "Blog Id" }));
        }

        Optional<BlogDTO> blog = blogService.getBlog(userId, blogId);
        if (blog.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST,
                    i18nMessages.getMessage(I18NMessage.NOT_EXISTS, new Object[] { "Blog" }));
        }
    }

    @After("@annotation(blogAccessTracker)")
    public void trackBlogAccess(JoinPoint joinPoint, BlogAccessTracker blogAccessTracker) throws AppException {
        Object[] args = joinPoint.getArgs();
        String userId = (String) args[blogAccessTracker.userIdPosition()];
        UserAccessDetails userAccessDetails = redisTemplate.opsForValue().get(userId);

        if (userAccessDetails != null) {
            userAccessDetails.setBlogAccessCount(userAccessDetails.getBlogAccessCount() + 1);
        } else {
            userAccessDetails = new UserAccessDetails();
            userAccessDetails.setBlogAccessCount(1);
            userAccessDetails.setUserId(userId);
        }
        LOGGER.info("User Access Details {}", userAccessDetails);
        if (userService.getUserType(userId) == UserType.GUEST && userAccessDetails.getBlogAccessCount() > 10) {
            throw new AccessLimitReachedException("Your daily read limit reached!", PageStatus.SIGNUP);
        }
        redisTemplate.opsForValue().set(userId, userAccessDetails, redisConfiguration.getExpireTime(),
                redisConfiguration.getExpireTimeUnit());
    }
}
