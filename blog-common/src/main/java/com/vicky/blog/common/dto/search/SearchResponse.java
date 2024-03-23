package com.vicky.blog.common.dto.search;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SearchResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private SearchDTO results;
    
}
