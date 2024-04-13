package com.vicky.blog.common.service;

import java.util.Optional;

import com.vicky.blog.common.dto.preference.PreferenceDTO;
import com.vicky.blog.common.dto.preference.PreferenceDTO.Theme;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;

public interface PreferenceService {
    
    Theme getUserTheme(String userId) throws AppException;

    void setUserTheme(String userId, Theme theme) throws AppException;
    
    PreferenceDTO enablePreferencesForUser(UserDTO userDTO) throws AppException;

    Optional<PreferenceDTO> getPreferences(String userId) throws AppException;

    PreferenceDTO getDefaultPreferences() throws AppException;

    PreferenceDTO updatePreferences(String userId, PreferenceDTO preferenceDTO) throws AppException;
}
