package com.vicky.blog.common.dto.tag;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TagsResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<TagDTO> tags;
}
