package com.vicky.blog.common.dto.follower;

import java.time.LocalDateTime;
import java.util.List;

import com.vicky.blog.common.dto.user.UserDTO;

import lombok.Data;

@Data
public class FollowersResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<UserDTO> followers;
}
