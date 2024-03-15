package com.vicky.blog.repository.firebase.model;

import lombok.Data;

@Data
public class FollowModal {
    
    private Long id;

    private Long user_profile_id;

    private Long follower_id;

    public static FollowModal build(com.vicky.blog.model.Follow follow) {
        FollowModal followModel = new FollowModal();
        followModel.setFollower_id(follow.getFollower().getId());
        followModel.setId(follow.getId());
        followModel.setUser_profile_id(follow.getUserProfile().getId());

        return followModel;
    }
}
