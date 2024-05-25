package com.vicky.blog.common.service;

import com.vicky.blog.common.dto.blog.BlogViewStats;
import com.vicky.blog.common.exception.AppException;

public interface BlogViewService {
    
    void addBlogView(String userId, String blogId) throws AppException;

    int getBlogViewCounts(String userId, String blogId) throws AppException;

    BlogViewStats getBlogViewStats(String userId, String blogId) throws AppException;
}
