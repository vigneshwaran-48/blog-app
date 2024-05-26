package com.vicky.blog.service.blog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogConstants;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogReadDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogReadService;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.BlogRead;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.BlogReadRepository;

@Service
public class BlogReadServiceImpl implements BlogReadService {

    @Autowired
    private BlogReadRepository blogReadRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void setReadTime(String userId, String blogId, long seconds) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if (user.isEmpty()) {
            user = userService.getUser(BlogConstants.GUEST_USER_ID);
        }
        BlogDTO blogDTO = blogService.getBlog(userId, blogId).get();

        BlogRead blogRead = new BlogRead();
        Optional<BlogRead> blogReadOptional = blogReadRepository.findByBlogIdAndReaderId(blogId, userId);
        if (blogReadOptional.isPresent()) {
            // Getting the previous user's blog read time in order to avoid duplicate entries.
            blogRead = blogReadOptional.get();
        }
        blogRead.setReader(User.build(user.get()));
        blogRead.setBlog(Blog.build(blogDTO));
        blogRead.setTimeSpent(seconds);
        blogRead.setReadedTime(LocalDateTime.now());
        BlogRead savedBlogRead = blogReadRepository.save(blogRead);
        if (savedBlogRead == null) {
            throw new AppException("Error while save reading time!");
        }
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public long getReadTime(String userId, String blogId) throws AppException {
        Optional<BlogRead> blogRead = blogReadRepository.findByBlogIdAndReaderId(blogId, userId);
        if (blogRead.isPresent()) {
            return blogRead.get().getTimeSpent();
        }
        // User not readed the blog before so giving the readed seconds as 0.
        return 0;
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public List<BlogReadDTO> getReadStatsOfBlog(String userId, String blogId) throws AppException {
        return blogReadRepository.findByBlogId(blogId).stream().map(BlogRead::toDTO).collect(Collectors.toList());
    }

}
