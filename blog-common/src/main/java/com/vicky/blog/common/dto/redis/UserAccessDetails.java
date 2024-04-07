package com.vicky.blog.common.dto.redis;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserAccessDetails implements Serializable {
    
    private String userId;
    private int blogAccessCount;
    
}
