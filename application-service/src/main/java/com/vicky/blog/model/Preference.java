package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.preference.PreferenceDTO;
import com.vicky.blog.common.dto.preference.PreferenceDTO.Theme;

import lombok.Data;

@Data
@Document
public class Preference {
    
    @Id
    private String id;

    @DocumentReference
    private User user;

    private Theme theme;

    public PreferenceDTO toDTO() {
        PreferenceDTO preferenceDTO = new PreferenceDTO();
        preferenceDTO.setId(id);
        preferenceDTO.setTheme(theme);
        preferenceDTO.setUserId(user.getId());
        return preferenceDTO;
    }

    public static Preference build(PreferenceDTO preferenceDTO) {
        Preference preference = new Preference();
        preference.setId(preferenceDTO.getId());
        preference.setTheme(preferenceDTO.getTheme());
        String userId = preferenceDTO.getUserId();
        User user = new User();
        user.setId(userId);
        preference.setUser(user);
        return preference;
    }
}
