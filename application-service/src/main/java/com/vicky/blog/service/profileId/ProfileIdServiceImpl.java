package com.vicky.blog.service.profileId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.repository.ProfileIdRepository;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Service
public class ProfileIdServiceImpl implements ProfileIdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileIdServiceImpl.class);

    @Autowired
    private ProfileIdRepository profileIdRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private I18NMessages i18NMessages;

    @Override
    public Optional<ProfileIdDTO> addProfileId(String entityId, String profileId, ProfileType type) throws AppException {
        checkProfileId(profileId);
        Optional<ProfileId> profileIdModel = profileIdRepository.findByEntityId(entityId);
        if(profileIdModel.isPresent()) {
            LOGGER.error("Already unique name {} exists for {}", profileIdModel.get().getProfileId(), entityId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Already unique name exists for " + entityId);
        }
        if(profileIdRepository.existsByProfileId(profileId)) {
            LOGGER.error("Already unique name {} exists", profileId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Unique name already exists");
        }
        ProfileId uNameModel = new ProfileId();
        uNameModel.setEntityId(entityId);
        uNameModel.setProfileId(profileId);
        uNameModel.setType(type);
        ProfileId savedUniqueName = profileIdRepository.save(uNameModel);
        if(savedUniqueName == null) {
            LOGGER.error("Error while saving unique name {}", profileId);
            throw new AppException("Error while saving unique name");
        }
        return Optional.of(savedUniqueName.toDTO());
    }

    @Override
    public boolean isProfileIdExists(String profileId) throws AppException {
        return profileIdRepository.existsByProfileId(profileId);
    }

    @Override
    public Optional<String> getProfileIdByEntityId(String entityId) throws AppException {
        Optional<ProfileId> profileIdModel = profileIdRepository.findByEntityId(entityId);
        if(profileIdModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(profileIdModel.get().getProfileId());
    }

    @Override
    public Optional<ProfileIdDTO> getProfileId(String profileId) throws AppException {
        checkProfileId(profileId);
        Optional<ProfileId> profileIdModel = profileIdRepository.findByProfileId(profileId);
        if(profileIdModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(profileIdModel.get().toDTO());
    }

    @Override
    public Optional<ProfileIdDTO> updateProfileId(String entityId, String profileId, ProfileType type) throws AppException {
        checkProfileId(profileId);
        Optional<ProfileId> profileIdModel = profileIdRepository.findByEntityId(entityId);
        if(profileIdModel.isEmpty()) {
            LOGGER.error("Unique Name not exists for the entity {} already", entityId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Unique Name not exists for the entity already");
        }
        Optional<ProfileIdDTO> ProfileIdDTO = getProfileId(profileId);
        if(ProfileIdDTO.isPresent() && !ProfileIdDTO.get().getEntityId().equals(entityId)) {
            LOGGER.error("Unique Name {} already exists", profileId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Unique Name already exists");
        }
        ProfileId uniqueNameToAdd = new ProfileId();
        uniqueNameToAdd.setId(profileIdModel.get().getId());
        uniqueNameToAdd.setEntityId(entityId);
        uniqueNameToAdd.setProfileId(profileId);
        uniqueNameToAdd.setType(type);

        ProfileId savedUniqueName = profileIdRepository.save(uniqueNameToAdd);
        if(savedUniqueName == null) {
            LOGGER.error("Error while updating unique name {} for entity {}", profileId, entityId);
            throw new AppException("Error while updating unique name");
        }
        return Optional.of(savedUniqueName.toDTO());
    }
    
    private void checkProfileId(String profileId) throws AppException {
        if(profileId == null) {
            Object[] args = { "Profile Id" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18NMessages.getMessage(I18NMessage.REQUIRED, args));
        }
    }

    @Override
    public void deleteProfileId(String entityId, String profileId) throws AppException {
        profileIdRepository.deleteByProfileIdAndEntityId(profileId, entityId);
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<ProfileIdDTO> getAllProfilesOfUser(String userId) throws AppException {
        Optional<ProfileId> profileId = profileIdRepository.findByEntityId(userId);
        List<OrganizationDTO> organizations = organizationService.getOrganizationsOfUser(userId);

        List<ProfileIdDTO> profiles = new ArrayList<>();
        profiles.add(profileId.get().toDTO());

        for(OrganizationDTO organization : organizations) {
            Optional<ProfileId> orgProfileId = profileIdRepository.findByEntityId(String.valueOf(organization.getId()));
            profiles.add(orgProfileId.get().toDTO());
        }
        return profiles;
    }
}
