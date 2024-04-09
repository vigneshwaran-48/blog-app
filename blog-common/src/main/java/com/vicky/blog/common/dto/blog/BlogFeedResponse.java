package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.blog.BlogFeedsDTO.PageStatus;

import lombok.Data;

@Data
public class BlogFeedResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time; 
    private BlogFeedDTO feed;
    private PageStatus blogStatus;

}
