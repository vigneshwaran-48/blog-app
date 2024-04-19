package com.vicky.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.preference.PreferenceDTO;
import com.vicky.blog.common.dto.preference.PreferenceDTO.Theme;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.PreferenceService;
import com.vicky.blog.model.Preference;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.PreferenceRepository;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Override
    public Theme getUserTheme(String userId) throws AppException {
        Optional<Preference> preferenceOptional = preferenceRepository.findByUserId(userId);
        if (preferenceOptional.isEmpty()) {
            return Theme.LIGHT;
        }
        return preferenceOptional.get().getTheme();
    }

    @Override
    @UserIdValidator(positions = 0)
    public void setUserTheme(String userId, Theme theme) throws AppException {
        Optional<Preference> preferenceOptional = preferenceRepository.findByUserId(userId);
        Preference preference = null;

        if (preferenceOptional.isEmpty()) {
            throw new AppException("Prefernces not exists for the user!");
        } else {
            preference = preferenceOptional.get();
        }
        
        preference.setTheme(theme);
        Preference saved = preferenceRepository.save(preference);
        if (saved == null) {
            throw new AppException("Unable to update the theme");
        }
    }

    @Override
    public PreferenceDTO enablePreferencesForUser(UserDTO userDTO) throws AppException {
        Preference preference = new Preference();
        preference.setTheme(Theme.LIGHT);
        preference.setUser(User.build(userDTO));

        Preference savedPreference = preferenceRepository.save(preference);
        if (savedPreference == null) {
            throw new AppException("Error while saving prefernce for the user");
        }
        return savedPreference.toDTO();
    }

    @Override
    public Optional<PreferenceDTO> getPreferences(String userId) throws AppException {
        Optional<Preference> preferenceOptional = preferenceRepository.findByUserId(userId);
        if (preferenceOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(preferenceOptional.get().toDTO());
    }

    @Override
    public PreferenceDTO getDefaultPreferences() throws AppException {
        PreferenceDTO preferenceDTO = new PreferenceDTO();
        preferenceDTO.setId("Guest_preference");
        preferenceDTO.setTheme(Theme.LIGHT);
        return preferenceDTO;
    }

    @Override
    @UserIdValidator(positions = 0)
    @CacheEvict(value = "users", key = "#userId")
    public PreferenceDTO updatePreferences(String userId, PreferenceDTO preferenceDTO) throws AppException {
        Optional<Preference> preferenceOptional = preferenceRepository.findByUserId(userId);
        preferenceDTO.setUserId(userId);
        if (preferenceOptional.isEmpty()) {
            throw new AppException("Preference not exists for the user!");
        }
        Preference existingPreference = preferenceOptional.get();
        preferenceDTO.setId(existingPreference.getId());
        if (preferenceDTO.getTheme() == null) {
            preferenceDTO.setTheme(existingPreference.getTheme());
        }
        Preference updatedPreference = preferenceRepository.save(Preference.build(preferenceDTO));
        return updatedPreference.toDTO();
    }
    
}
