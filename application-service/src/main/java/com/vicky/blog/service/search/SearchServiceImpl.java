package com.vicky.blog.service.search;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.search.SearchDTO;
import com.vicky.blog.common.dto.search.SearchDTO.SearchType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchUtil searchUtil;

    @Override
    public SearchDTO search(String userId, String query, SearchType type) throws AppException {
        Optional<SearchDTO> result = searchUtil.search(userId, query, type);
        if (result.isEmpty()) {
            SearchDTO searchDTO = new SearchDTO(type, new ArrayList<>());
            return searchDTO;
        }
        return result.get();
    }
    
}
