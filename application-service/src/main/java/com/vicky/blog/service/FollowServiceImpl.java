package com.vicky.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.ProfileAccessValidator;
import com.vicky.blog.annotation.ProfileIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.model.Follow;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.repository.FollowRepository;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private FollowRepository followRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FollowServiceImpl.class);

    @Override
    @UserIdValidator(positions = 0)
    @ProfileIdValidator(positions = 1)
    @ProfileAccessValidator(userIdPosition = 0, profileIdPosition = 1)
    public void followProfile(String userId, String profileId) throws AppException {
        
        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).get();
        String userProfileId = profileIdService.getProfileIdByEntityId(userId).get();
        ProfileIdDTO userProfile = profileIdService.getProfileId(userProfileId).get();
        if(profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Checking user has access to this organization...
            organizationService.getOrganization(userId, Long.parseLong(profileIdDTO.getEntityId()));
        }
        else {
            if(userProfile.getProfileId().equals(profileId)) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, "You can't follow yourself!");
            }
        }
        Follow follow = new Follow();
        follow.setFollower(ProfileId.build(userProfile));
        follow.setUserProfile(ProfileId.build(profileIdDTO));
        Follow savedFollow = followRepository.save(follow);
        if(savedFollow == null) {
            LOGGER.error("Saved follow was null for the profile {} and follow {}", profileId, userProfileId);
            throw new AppException("Error while following the user");
        }
    }

    @Override
    @UserIdValidator(positions = 0)
    @ProfileIdValidator(positions = 1)
    @ProfileAccessValidator(userIdPosition = 0, profileIdPosition = 1)
    public List<FollowDTO> getFollowersOfProfile(String userId, String profileId) throws AppException {
        List<Follow> followers = followRepository.findByUserProfileProfileId(profileId);
        return followers.stream().map(follow -> follow.toDTO()).collect(Collectors.toList());
    }
    
}
