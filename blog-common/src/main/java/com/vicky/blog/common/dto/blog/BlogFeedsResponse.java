package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;
import java.util.List;

import com.vicky.blog.common.dto.blog.BlogFeedsDTO.PageStatus;

import lombok.Data;

@Data
public class BlogFeedsResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time; 
    private List<BlogDTO> blogs;
    private PageStatus nextPageStatus;

}
