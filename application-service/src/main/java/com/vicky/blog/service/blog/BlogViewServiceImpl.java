package com.vicky.blog.service.blog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogViewDTO;
import com.vicky.blog.common.dto.blog.BlogViewStats;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.BlogViewService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.BlogView;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.BlogViewRepository;

@Service
public class BlogViewServiceImpl implements BlogViewService {

    @Autowired
    private BlogViewRepository blogViewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogViewServiceImpl.class);

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void addBlogView(String userId, String blogId) throws AppException {
        List<BlogView> userBlogViews = blogViewRepository.findByBlogIdAndUserIdOrderByViewedTimeDesc(blogId, userId);

        boolean isUserViewedBlogToday =
                !userBlogViews.isEmpty() && userBlogViews.get(0).getViewedTime().toLocalDate().isEqual(LocalDate.now());
        if (isUserViewedBlogToday) {
            LOGGER.info("User view count for today has been stored already!");
            return;
        }
        Optional<UserDTO> user = userService.getUser(userId);
        if (user.isEmpty()) {
            user = userService.getUser(com.vicky.blog.common.dto.blog.BlogConstants.GUEST_USER_ID);
        }
        BlogDTO blogDTO = blogService.getBlog(userId, blogId).get();
        BlogView blogView = new BlogView();
        blogView.setBlog(Blog.build(blogDTO));
        blogView.setUser(User.build(user.get()));
        blogView.setViewedTime(LocalDateTime.now());
        blogViewRepository.save(blogView);
        LOGGER.info("Added blog view for user");
    }

    @Override
    public int getBlogViewCounts(String userId, String blogId) throws AppException {
        List<BlogView> blogViews = blogViewRepository.findByBlogId(blogId);
        return blogViews.size();
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public BlogViewStats getBlogViewStats(String userId, String blogId) throws AppException {
        List<BlogView> blogViews = blogViewRepository.findByBlogId(blogId);
        BlogViewStats blogViewStats = new BlogViewStats();
        blogViewStats.setBlogId(blogId);
        blogViewStats.setViewsCount(blogViews.size());
        List<BlogViewDTO> blogViewDTOs = new ArrayList<>();
        
        Set<String> userIds = new HashSet<>();
        for (BlogView blogView : blogViews) {
            userIds.add(blogView.getUser().getId());
            blogViewDTOs.add(blogView.toDTO());
        }
        blogViewStats.setBlogViews(blogViewDTOs);
        blogViewStats.setUsersCount(userIds.size());
        return blogViewStats;
    }

}
