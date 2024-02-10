package com.vicky.blog.service.blog;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.repository.BlogRepository;

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
        
        UserDTO user = userService.getUser(blogDTO.getOwnerId())
                                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "Owner not exists"));

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
    public Optional<BlogDTO> getBlog(Long id) throws AppException {
        
        Optional<Blog> blog = blogRepository.findById(id);

        if(blog.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(blog.get().toDTO());
    }

    @Override
    public Optional<BlogDTO> updateBlog(BlogDTO blogDTO) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlog'");
    }

    @Override
    public void deleteBlog(Long id) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBlog'");
    }
    
}
