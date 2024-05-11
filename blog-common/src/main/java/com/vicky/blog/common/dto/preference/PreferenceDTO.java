package com.vicky.blog.common.dto.preference;

import java.io.Serializable;

import lombok.Data;

@Data
public class PreferenceDTO implements Serializable {

    public enum Theme {
        DARK,
        LIGHT
    }
    
    private String id;
    private String userId;
    private Theme theme;
}
