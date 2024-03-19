package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.exception.AppException;

public interface BlogService {
    
    String addBlog(String userId, BlogDTO blogDTO) throws AppException;

    Optional<BlogDTO> getBlog(String userId, String id) throws AppException;

    Optional<BlogDTO> updateBlog(String userId, BlogDTO blogDTO) throws AppException;

    void deleteBlog(String userId, String id) throws AppException;

    List<BlogDTO> getAllBlogsOfUser(String userId) throws AppException;

    void publishBlog(String userId, String blogId, String publishAt) throws AppException;

    void unPublishBlog(String userId, String blogId) throws AppException;

    Optional<BlogDTO> getBlogOfProfile(String userId, String blogId, String profileId) throws AppException;

    List<BlogDTO> getAllBlogsOfProfile(String userId, String profileId) throws AppException;
}
