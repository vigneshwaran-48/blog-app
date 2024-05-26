package com.vicky.blog.common.dto.blog;

import java.util.List;

import lombok.Data;

@Data
public class BlogViewStats {
    
    private String blogId;
    private String title;
    private int viewsCount;
    private int usersCount;
    private List<BlogViewDTO> blogViews;
}
