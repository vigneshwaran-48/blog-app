package com.vicky.blog.service.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.search.SearchDTO;
import com.vicky.blog.common.dto.search.SearchDTO.SearchBy;
import com.vicky.blog.common.dto.search.SearchDTO.SearchType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchUtil searchUtil;

    @Override
    public SearchDTO search(String userId, String query, SearchType type, List<SearchBy> searchBy) throws AppException {
        Optional<SearchDTO> result = searchUtil.search(userId, query, type, searchBy);
        if (result.isEmpty()) {
            SearchDTO searchDTO = new SearchDTO(new ArrayList<>());
            return searchDTO;
        }
        return result.get();
    }

    @Override
    public SearchDTO search(String userId, String query, List<SearchType> type, List<SearchBy> searchBy)
            throws AppException {
        SearchDTO searchDTO = new SearchDTO();
        for (SearchType searchType : type) {
            SearchDTO currentSearch = search(userId, query, searchType, searchBy);
            if(currentSearch.getEntities() == null) {
                continue;
            }
            if (searchDTO.getEntities() != null) {
                searchDTO.getEntities().addAll(currentSearch.getEntities());
            } else {
                searchDTO.setEntities(currentSearch.getEntities());
            }
        }
        return searchDTO;
    }
    
}
