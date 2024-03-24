package com.vicky.blog.common.service;

import java.util.List;

import com.vicky.blog.common.dto.search.SearchDTO;
import com.vicky.blog.common.dto.search.SearchDTO.SearchType;
import com.vicky.blog.common.exception.AppException;

public interface SearchService {
    
    SearchDTO search(String userId, String query, SearchType type) throws AppException;
}
