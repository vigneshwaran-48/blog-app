package com.vicky.blog.common.dto.preference;

import lombok.Data;

@Data
public class PreferenceDTO {

    public enum Theme {
        DARK,
        LIGHT
    }
    
    private String id;
    private String userId;
    private Theme theme;
}
