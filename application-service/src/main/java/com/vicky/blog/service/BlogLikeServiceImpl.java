package com.vicky.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogLikeService;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.BlogLike;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.BlogLikeMongoRepository;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Service
public class BlogLikeServiceImpl implements BlogLikeService {

    @Autowired
    private BlogLikeMongoRepository blogLikeRepository;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private I18NMessages i18nMessages;

    private static final Logger LOG = LoggerFactory.getLogger(BlogLikeServiceImpl.class);

    @Override
    @UserIdValidator(positions = 1)
    public Optional<BlogLikeDTO> addLike(String blogId, String userId, String profileId) throws AppException {
        UserDTO user = userService.getUser(userId).get();
        BlogDTO blog = getBlog(userId, profileId, blogId);

        Optional<BlogLike> blogLikeOptional = blogLikeRepository.findByBlogIdAndLikedById(blogId, userId);
        if(blogLikeOptional.isPresent()) {
            return Optional.of(blogLikeOptional.get().toDTO());
        }

        BlogLike blogLike = new BlogLike();
        blogLike.setBlog(Blog.build(blog));
        blogLike.setLikedBy(User.build(user));

        BlogLike savedBlogLike = blogLikeRepository.save(blogLike);
        if(savedBlogLike == null) {
            LOG.error("Error while adding like to blog {} for user {}", blogId, userId);
            throw new AppException("Error while adding like");
        }
        return Optional.of(savedBlogLike.toDTO());
    }

    @Override
    public void removeLike(String blogId, String userId, String profileId) throws AppException {
        // Just validating given details.
        getUser(userId);
        getBlog(userId, profileId, blogId);

        blogLikeRepository.deleteByBlogIdAndLikedById(blogId, userId);
    }

    @Override
    public List<BlogLikeDTO> getLikesOfBlog(String userId, String blogId, String profileId) throws AppException {
        // Just validating given details.
        getBlog(userId, profileId, blogId);

        return blogLikeRepository.findByBlogId(blogId)
                                .stream()
                                .map(blogLike -> blogLike.toDTO())
                                .collect(Collectors.toList());
    }
    
    private UserDTO getUser(String userId) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            LOG.error("User {} not exists", userId);
            Object[] args = { "User " + userId };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NOT_EXISTS, args));
        }
        return user.get();
    }

    private BlogDTO getBlog(String userId, String profileId, String blogId) throws AppException {
        Optional<BlogDTO> blog = blogService.getBlogOfProfile(userId, blogId, profileId);
        if(blog.isEmpty()) {
            LOG.error("Blog {} not exists", userId);
            Object[] args = { "Blog " + blogId };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NOT_EXISTS, args));
        }
        return blog.get();
    }
}
