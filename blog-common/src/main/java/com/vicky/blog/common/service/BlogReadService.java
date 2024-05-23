package com.vicky.blog.common.service;

import java.util.List;

import com.vicky.blog.common.dto.blog.BlogReadDTO;
import com.vicky.blog.common.exception.AppException;

public interface BlogReadService {
    
    void setReadTime(String userId, String blogId, long seconds) throws AppException;

    long getReadTime(String userId, String blogId) throws AppException;

    List<BlogReadDTO> getReadStatsOfBlog(String userId, String blogId) throws AppException;
}
