package com.vicky.blog.service.blog;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.repository.BlogRepository;

@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogUtil blogUtil;

    @Override
    public Long addBlog(BlogDTO blogDTO) throws AppException {
        
        UserDTO user = null;

        try {
            user = userService.getUser(blogDTO.getOwnerId())
                                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "Owner not exists"));
        } 
        catch (Exception e) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Invalid owner");
        }

        blogUtil.validateBlogData(blogDTO);
        Blog blog = Blog.build(blogDTO, user);

        Blog savedBlog = blogRepository.save(blog);

        if(savedBlog == null) {
            LOGGER.error("Error while adding blog {}", blog);
            throw new AppException("Errow while adding blog!");
        }
        return savedBlog.getId();
    }

    @Override
    public Optional<BlogDTO> getBlog(String userId, Long id) throws AppException {
        
        Optional<Blog> blog = blogRepository.findByUserIdAndId(userId, id);

        if(blog.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(blog.get().toDTO());
    }

    @Override
    public Optional<BlogDTO> updateBlog(BlogDTO blogDTO) throws AppException {
        
        Optional<BlogDTO> existingBlog = getBlog(blogDTO.getOwnerId(), blogDTO.getId());

        if(existingBlog.isEmpty()) {
            LOGGER.error("Blog {} not exists", blogDTO.getId());
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Blog not exists");
        }

        blogUtil.checkAndFillMissingDataForPatchUpdate(blogDTO, existingBlog.get());

        Blog savedBlog = blogRepository.save(Blog.build(blogDTO, userService.getUser(blogDTO.getOwnerId()).get()));

        if(savedBlog == null) {
            LOGGER.error("Error while saving blog {}", blogDTO);
            throw new AppException("Errow while saving blog!");
        }
        return Optional.of(savedBlog.toDTO());
    }

    @Override
    public void deleteBlog(String userId, Long id) throws AppException {
        blogRepository.deleteByUserIdAndId(userId, id);
    }
    
}
