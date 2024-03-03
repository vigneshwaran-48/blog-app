package com.vicky.blog.common.dto.profile;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileIdsResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<ProfileIdDTO> profiles;

}
