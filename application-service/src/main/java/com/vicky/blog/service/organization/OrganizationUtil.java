package com.vicky.blog.service.organization;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.model.Organization;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;
import com.vicky.blog.service.profileId.ProfileIdUtil;

@Component
class OrganizationUtil {
    @Autowired
    private I18NMessages i18nMessages;
    @Autowired
    private ProfileIdUtil profileIdUtil;
    @Autowired
    private ProfileIdService profileIdService;
    
    public void validateOrganizationData(OrganizationDTO organizationDTO) throws AppException {
        profileIdUtil.validateUniqueName(String.valueOf(organizationDTO.getId()), organizationDTO.getProfileId());
        validateOrganizationName(organizationDTO.getName());
        validateOrganizationDescription(organizationDTO.getDescription());
    }

    public void checkAndFillMissingDataForPatchUpdate(OrganizationDTO newData, Organization existingData) throws AppException {
        // Not allowing to change the created time of the organization
        newData.setCreatedTime(existingData.getCreatedTime());

        if(newData.getDescription() == null) {
            newData.setDescription(existingData.getDescription());
        }
        if(newData.getImage() == null) {
            newData.setImage(existingData.getImage());
        }
        if(newData.getJoinType() == null) {
            newData.setJoinType(existingData.getJoinType());
        }
        if(newData.getName() == null) {
            newData.setName(existingData.getName());
        }
        if(newData.getOwner() == null) {
            newData.setOwner(existingData.getOwner().toDTO());
        }
        if(newData.getVisibility() == null) {
            newData.setVisibility(existingData.getVisibility());
        }
        if(newData.getProfileId() == null) {
            Optional<String> profileId = profileIdService.getProfileIdByEntityId(String.valueOf(existingData.getId()));
            if(profileId.isPresent()) {
                newData.setProfileId(profileId.get());
            }
            else {
                newData.setProfileId(String.valueOf(existingData.getId()));
            }
        } else {
            Optional<ProfileIdDTO> profileIdDTO = profileIdService.getProfileId(newData.getProfileId());
            if (!profileIdDTO.isEmpty() && !profileIdDTO.get().getEntityId().equals(newData.getId())) {
                throw new AppException("That profile id already taken!");
            }
        }
    }

    private void validateOrganizationName(String name) throws AppException {
        if(name != null) {
            if(name.length() < OrganizationConstants.NAME_MIN_LENGTH
                || name.length() > OrganizationConstants.NAME_MAX_LENGTH) {

                Object[] args = { "Organization name",
                                    OrganizationConstants.NAME_MIN_LENGTH, OrganizationConstants.NAME_MAX_LENGTH };
                throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.MIN_MAX, args));
            }
        }
        else {
            Object[] args = { "Organization name" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NAME_REQUIRED, args));
        }
    }

    private void validateOrganizationDescription(String description) throws AppException {
        if(description != null) {
            if(description.length() > OrganizationConstants.DESCRIPTION_MAX_LENGTH) {
                Object[] args = { "Description", OrganizationConstants.DESCRIPTION_MAX_LENGTH };

                throw new AppException(HttpStatus.SC_BAD_REQUEST,
                        i18nMessages.getMessage(I18NMessage.MAX_LENGTH, args));
            }
        }
    }
}
