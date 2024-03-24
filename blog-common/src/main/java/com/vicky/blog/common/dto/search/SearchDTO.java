package com.vicky.blog.common.dto.search;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDTO {

    public enum SearchType {
        USER, ORGANIZATION, BLOG
    }
    
    private SearchType type;
    private List<String> entitieIds;
}
