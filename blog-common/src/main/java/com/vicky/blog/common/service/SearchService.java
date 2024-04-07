package com.vicky.blog.common.service;

import com.vicky.blog.common.dto.search.SearchDTO;
import com.vicky.blog.common.dto.search.SearchDTO.SearchBy;
import com.vicky.blog.common.dto.search.SearchDTO.SearchType;
import com.vicky.blog.common.exception.AppException;
import java.util.List;

public interface SearchService {
    SearchDTO search(String userId, String query, SearchType type, List<SearchBy> searchBy) throws AppException;

    SearchDTO search(String userId, String query, List<SearchType> type, List<SearchBy> searchBy)
            throws AppException;
}
