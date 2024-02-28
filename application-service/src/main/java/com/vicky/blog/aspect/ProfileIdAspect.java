package com.vicky.blog.aspect;

import java.util.Optional;

import org.apache.http.HttpStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.vicky.blog.annotation.ProfileAccessValidator;
import com.vicky.blog.annotation.ProfileIdValidator;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.exception.OrganizationNotAccessible;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Aspect
@Configuration
public class ProfileIdAspect {

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private I18NMessages i18nMessages;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileIdAspect.class);
    
    @Before("@annotation(profileIdValidator)")
    public void profileIdValidator(JoinPoint joinPoint, ProfileIdValidator profileIdValidator) throws AppException {
        int[] positionsToCheck = profileIdValidator.positions();

        Object[] args = joinPoint.getArgs();

        for(int position : positionsToCheck) {
            String id = (String) args[position];

            if(id == null) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "Profile Id" }));
            }

            Optional<ProfileIdDTO> profile = profileIdService.getProfileId(id);
            if(profile.isEmpty()) {
                LOGGER.error("Profile {} not exists", id);
                throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.NOT_EXISTS, 
                    new Object[] { "Profile" }));
            }
        }
    }

    @Before("@annotation(profileAccessValidator)")
    public void userProfileAccessValidator(JoinPoint joinPoint, ProfileAccessValidator profileAccessValidator) 
        throws AppException {

        int userIdPosition = profileAccessValidator.userIdPosition();
        int profileIdPosition = profileAccessValidator.profileIdPosition();

        boolean userIdMissing = userIdPosition < 0 && profileIdPosition >= 0;
        boolean profileIdMissing = userIdPosition >= 0 && profileIdPosition < 0;
        boolean bothAreMissing = userIdPosition < 0 && profileIdPosition < 0;

        if(userIdMissing || profileIdMissing || bothAreMissing) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, 
                    new Object[] { "User Id and Profile Id" }));
        }

        Object[] args = joinPoint.getArgs();
        String userId = args[userIdPosition].toString();
        String profileId = args[profileIdPosition].toString();

        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).get();
        if(profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Checking user has access to this organization...
            organizationService.getOrganization(userId, Long.parseLong(profileIdDTO.getEntityId()));
        }
    }
}
