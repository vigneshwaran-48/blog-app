package com.vicky.blog.common.dto.blog;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BlogReadStatsResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<BlogReadDTO> blogReads;
}
