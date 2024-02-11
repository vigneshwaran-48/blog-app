package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.exception.AppException;

public interface BlogService {
    
    Long addBlog(BlogDTO blogDTO) throws AppException;

    Optional<BlogDTO> getBlog(String userId, Long id) throws AppException;

    Optional<BlogDTO> updateBlog(BlogDTO blogDTO) throws AppException;

    void deleteBlog(String userId, Long id) throws AppException;

    List<BlogDTO> getAllBlogsOfUser(String userId) throws AppException;
}
