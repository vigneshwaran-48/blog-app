package com.vicky.blog.model;

import com.vicky.blog.common.dto.follower.FollowDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Follow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "user_profile_id", nullable = false)
    @ManyToOne
    private ProfileId userProfile;

    @JoinColumn(name = "follower_id", nullable = false)
    @ManyToOne
    private ProfileId follower;

    public FollowDTO toDTO() {
        FollowDTO followDTO = new FollowDTO();
        followDTO.setId(id);
        followDTO.setFollower(follower.toDTO());
        followDTO.setProfile(userProfile.toDTO());
        return followDTO;
    }
}
