package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BlogFeedsResponse {

    public enum PageStatus {
        AVAILABLE, SINGUP, BUY_PREMIUM
    }
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time; 
    private List<BlogDTO> blogs;
    private PageStatus nextPageStatus;

}
