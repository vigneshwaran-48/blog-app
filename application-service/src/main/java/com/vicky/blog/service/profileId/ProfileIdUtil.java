package com.vicky.blog.service.profileId;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Component
public class ProfileIdUtil {

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private I18NMessages i18nMessages;

    private static final String SPACE = " ";
    private static final String HASH = "#";
    
    public void validateUniqueName(String entityId, String profileId) throws AppException {
        if(profileId == null) {
            return;
        }
        if(profileId.contains(SPACE) || profileId.contains(HASH)) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Space not allowed in profile id");
        }
        Optional<ProfileIdDTO> uniqueNameDTO = profileIdService.getProfileId(profileId);
        if(uniqueNameDTO.isPresent()) {
            if(uniqueNameDTO.get().getEntityId().equals(entityId)) {
                return;
            }
            Object[] args = { "Profile Id" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.EXISTS, args));
        }
    }
}
