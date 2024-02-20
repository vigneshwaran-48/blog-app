package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;
import com.vicky.blog.common.exception.AppException;

public interface BlogLikeService {
    
    Optional<BlogLikeDTO> addLike(Long blogId, String userId) throws AppException;

    void removeLike(Long blogId, String userId) throws AppException;

    List<BlogLikeDTO> getLikesOfBlog(String userId, Long blogId) throws AppException;
}