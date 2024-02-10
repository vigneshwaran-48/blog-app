package com.vicky.blog.common.dto.blog;

import lombok.Data;

@Data
public class BlogDTO {
    
    private Long id;
    private String title;
    private String content;
    private String image;
    private String ownerId;

}
