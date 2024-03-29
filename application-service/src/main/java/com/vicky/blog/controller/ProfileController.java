package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.dto.follower.FollowersResponse;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileResponse;
import com.vicky.blog.common.dto.profile.ProfileIdsResponse;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/profile")
public class ProfileController {

    @Autowired
    private UserIdExtracter userIdExtracter;

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private FollowService followService;
    
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String profileId, Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        Optional<ProfileIdDTO> profileIdDTO = profileIdService.getProfileId(profileId);
        if(profileIdDTO.isEmpty()) {
            throw new AppException(HttpStatus.SC_NOT_FOUND, "Profile not exists");
        }

        String entityId = profileIdDTO.get().getEntityId();
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileId(profileId);
        profileDTO.setEntityId(entityId);
        profileDTO.setType(profileIdDTO.get().getType());
        
        if(profileIdDTO.get().getType() == ProfileType.USER) {
            Optional<UserDTO> user = userService.getUser(entityId);
            profileDTO.setName(user.get().getName());
            profileDTO.setDescription(user.get().getDescription());
            profileDTO.setBannerImage(user.get().getImage());
        }
        else {
            Optional<OrganizationDTO> organization = organizationService.getOrganization(userId, entityId);
            profileDTO.setName(organization.get().getName());
            profileDTO.setDescription(organization.get().getDescription()); 
            profileDTO.setBannerImage(organization.get().getImage());
        }
        ProfileResponse response = ProfileResponse.builder()
                                                    .status(HttpStatus.SC_OK)
                                                    .message("success")
                                                    .path(request.getServletPath())
                                                    .time(LocalDateTime.now())
                                                    .profile(profileDTO)
                                                    .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<ProfileIdsResponse> getAllProfilesOfUser(Principal principal, HttpServletRequest request) 
        throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<ProfileIdDTO> profiles = profileIdService.getAllProfilesOfUser(userId);

        ProfileIdsResponse response = ProfileIdsResponse.builder()
                                                    .status(HttpStatus.SC_OK)
                                                    .message("success")
                                                    .path(request.getServletPath())
                                                    .time(LocalDateTime.now())
                                                    .profiles(profiles)
                                                    .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{profileId}/follow")
    public ResponseEntity<EmptyResponse> followProfile(@PathVariable String profileId, Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        followService.followProfile(userId, profileId);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Followed profile " + profileId + "!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{profileId}/follow")
    public ResponseEntity<FollowersResponse> getFollowersOfProfile(@PathVariable String profileId, Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<FollowDTO> followers = followService.getFollowersOfProfile(userId, profileId);
        List<UserDTO> followerUserDTOs = new ArrayList<>();

        for(FollowDTO follower : followers) {
            UserDTO user = userService.getUser(follower.getFollower().getEntityId()).get();
            followerUserDTOs.add(user);
        }

        FollowersResponse response = new FollowersResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setFollowers(followerUserDTOs);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{profileId}/follow")
    public ResponseEntity<EmptyResponse> unFollowProfile(@PathVariable String profileId, Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        followService.unFollowProfile(userId, profileId);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("UnFollowed profile " + profileId + "!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
}
