package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;
import com.vicky.blog.common.exception.AppException;

public interface BlogLikeService {
    
    Optional<BlogLikeDTO> addLike(String blogId, String userId, String profileId) throws AppException;

    void removeLike(String blogId, String userId, String profileId) throws AppException;

    List<BlogLikeDTO> getLikesOfBlog(String userId, String blogId, String profileId) throws AppException;

    List<BlogDTO> getMostLikedBlogs(String userId) throws AppException;
}