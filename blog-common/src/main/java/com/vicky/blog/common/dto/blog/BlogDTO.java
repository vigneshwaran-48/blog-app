package com.vicky.blog.common.dto.blog;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class BlogDTO implements Serializable {
    
    private String id;
    private String title;
    private String content;
    private String image;
    private UserDTO owner;
    private String description;
    private LocalDateTime postedTime;
    private String displayPostedDate;
    private String postedProfileId;
    private boolean isPublised;
    private ProfileIdDTO publishedAt;

}
