package com.vicky.blog.service.blog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        
        try {
            userService.getUser(blogDTO.getOwner().getId())
                                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "Owner not exists"));
        } 
        catch (Exception e) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Invalid owner");
        }

        blogDTO.setDescription(blogUtil.getDescriptionForBlog(blogDTO.getContent()));
        blogUtil.validateBlogData(blogDTO);

        blogDTO.setPostedTime(LocalDateTime.now());

        Blog blog = Blog.build(blogDTO);
        Blog savedBlog = blogRepository.save(blog);

        if(savedBlog == null) {
            LOGGER.error("Error while adding blog {}", blog);
            throw new AppException("Errow while adding blog!");
        }
        return savedBlog.getId();
    }

    @Override
    public Optional<BlogDTO> getBlog(String userId, Long id) throws AppException {
        
        Optional<Blog> blog = blogRepository.findByOwnerIdAndId(userId, id);

        if(blog.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(blog.get().toDTO());
    }

    @Override
    public Optional<BlogDTO> updateBlog(BlogDTO blogDTO) throws AppException {
        
        Optional<BlogDTO> existingBlog = getBlog(blogDTO.getOwner().getId(), blogDTO.getId());

        if(existingBlog.isEmpty()) {
            LOGGER.error("Blog {} not exists", blogDTO.getId());
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Blog not exists");
        }

        blogUtil.checkAndFillMissingDataForPatchUpdate(blogDTO, existingBlog.get());
        blogUtil.validateBlogData(blogDTO);

        Blog savedBlog = blogRepository.save(Blog.build(blogDTO));

        if(savedBlog == null) {
            LOGGER.error("Error while saving blog {}", blogDTO);
            throw new AppException("Errow while saving blog!");
        }
        return Optional.of(savedBlog.toDTO());
    }

    @Override
    public void deleteBlog(String userId, Long id) throws AppException {
        blogRepository.deleteByOwnerIdAndId(userId, id);
    }

    @Override
    public List<BlogDTO> getAllBlogsOfUser(String userId) throws AppException {
        List<Blog> blogs = blogRepository.findByOwnerId(userId);

        if(blogs.isEmpty()) {
            return List.of();
        }
        return blogs.stream().map(blog -> blog.toDTO()).collect(Collectors.toList());
    }
    
}
