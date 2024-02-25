package com.vicky.blog.common.service;

import java.util.Optional;

import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.exception.AppException;

public interface ProfileIdService {
    
    Optional<ProfileIdDTO> addProfileId(String entityId, String profileId, ProfileType type) throws AppException;
    boolean isProfileIdExists(String profileId) throws AppException;
    Optional<String> getProfileIdByEntityId(String entityId) throws AppException;
    Optional<ProfileIdDTO> getProfileId(String profileId) throws AppException;
    Optional<ProfileIdDTO> updateProfileId(String entityId, String profileId, ProfileType type) throws AppException;
    void deleteProfileId(String entityId, String profileId) throws AppException;
}
