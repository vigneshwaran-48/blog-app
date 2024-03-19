package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.follower.FollowDTO;

import lombok.Data;

@Document
@Data
public class Follow {
    
    @Id
    private String id;

    @DocumentReference
    private ProfileId userProfile;

    @DocumentReference
    private ProfileId follower;

    public FollowDTO toDTO() {
        FollowDTO followDTO = new FollowDTO();
        followDTO.setId(id);
        followDTO.setFollower(follower.toDTO());
        followDTO.setProfile(userProfile.toDTO());
        return followDTO;
    }
}
