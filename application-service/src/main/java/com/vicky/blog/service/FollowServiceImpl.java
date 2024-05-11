package com.vicky.blog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Follow;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.FollowMongoRepository;
import com.vicky.blog.repository.mongo.UserMongoRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private FollowMongoRepository followRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMongoRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FollowServiceImpl.class);

    @Override
    @UserIdValidator(positions = 0)
    @ProfileIdValidator(positions = 1)
    @ProfileAccessValidator(userIdPosition = 0, profileIdPosition = 1)
    public void followProfile(String userId, String profileId) throws AppException {

        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).get();
        String userProfileId = profileIdService.getProfileIdByEntityId(userId).get();
        ProfileIdDTO userProfile = profileIdService.getProfileId(userProfileId).get();
        if (profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Checking user has access to this organization...
            organizationService.getOrganization(userId, profileIdDTO.getEntityId());
        } else {
            if (userProfile.getProfileId().equals(profileId)) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, "You can't follow yourself!");
            }
        }
        Follow follow = new Follow();
        follow.setFollower(ProfileId.build(userProfile));
        follow.setUserProfile(ProfileId.build(profileIdDTO));
        Follow savedFollow = followRepository.save(follow);
        if (savedFollow == null) {
            LOGGER.error("Saved follow was null for the profile {} and follow {}", profileId, userProfileId);
            throw new AppException("Error while following the user");
        }
    }

    @Override
    @UserIdValidator(positions = 0)
    @ProfileIdValidator(positions = 1)
    @ProfileAccessValidator(userIdPosition = 0, profileIdPosition = 1)
    public List<FollowDTO> getFollowersOfProfile(String userId, String profileId) throws AppException {
        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).get();
        List<Follow> followers = followRepository.findByUserProfileId(profileIdDTO.getId());
        return followers.stream().map(follow -> follow.toDTO()).collect(Collectors.toList());
    }

    @Override
    @UserIdValidator(positions = 0)
    @ProfileIdValidator(positions = 1)
    @ProfileAccessValidator(userIdPosition = 0, profileIdPosition = 1)
    public void unFollowProfile(String userId, String profileId) throws AppException {
        ProfileIdDTO userProfile = profileIdService.getProfileByEntityId(userId).get();
        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).get();

        followRepository.deleteByUserProfileIdAndFollowerId(profileIdDTO.getId(), userProfile.getId());
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<UserDTO> getAllFollowingUsers(String userId) throws AppException {
        ProfileIdDTO userProfile = profileIdService.getProfileByEntityId(userId).get();
        List<Follow> following = followRepository.findByFollowerId(userProfile.getId());
        List<UserDTO> followingUsers = new ArrayList<>();
        LOGGER.info(following + "");
        for (Follow follow : following) {
            if (follow.getUserProfile().getType() != ProfileType.USER) {
                continue;
            }
            Optional<UserDTO> userOptional = userService.getUser(follow.getUserProfile().getEntityId());
            if (userOptional.isEmpty()) {
                throw new AppException("The following user is not exists!");
            }
            followingUsers.add(userOptional.get());
        }
        return followingUsers;
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<UserDTO> getMostFollowedUsers(String userId) throws AppException {
        @Data
        @AllArgsConstructor
        class UserFollowersCount {
            private int follwersCount;
            private User user;
        }
        return userRepository.findAll().stream().map(user -> {
            List<FollowDTO> followers = new ArrayList<>();
            try {
                ProfileIdDTO profileIdDTO = profileIdService.getProfileByEntityId(user.getId()).get();
                followers = getFollowersOfProfile(user.getId(), profileIdDTO.getProfileId());
            } catch (AppException e) {
                LOGGER.error(e.getMessage(), e);
            }
            return new UserFollowersCount(followers.size(), user);
        }).sorted((a, b) -> Integer.compare(a.follwersCount, b.follwersCount)).limit(5)
                .map(userFollowersCount -> userFollowersCount.user.toDTO()).collect(Collectors.toList());
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<UserDTO> getFollowingUsers(String userId) throws AppException {
        ProfileIdDTO profileIdDTO = profileIdService.getProfileByEntityId(userId).get();
        return followRepository.findByFollowerId(profileIdDTO.getId()).stream()
                .filter(follow -> follow.getUserProfile().getType() == ProfileType.USER).map(follow -> {
                    UserDTO user = null;
                    try {
                        user = userService.getUser(follow.getUserProfile().getEntityId()).get();
                    } catch (AppException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    return user;
                }).collect(Collectors.toList());
    }

    @Override
    public List<OrganizationDTO> getFollowingOrganizations(String userId) throws AppException {
        ProfileIdDTO profileIdDTO = profileIdService.getProfileByEntityId(userId).get();
        return followRepository.findByFollowerId(profileIdDTO.getId()).stream()
                .filter(follow -> follow.getUserProfile().getType() == ProfileType.ORGANIZATION).map(follow -> {
                    OrganizationDTO organization = null;
                    try {
                        organization = organizationService
                                .getOrganization(userId, follow.getUserProfile().getEntityId()).get();
                    } catch (AppException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    return organization;
                }).collect(Collectors.toList());
    }

}
