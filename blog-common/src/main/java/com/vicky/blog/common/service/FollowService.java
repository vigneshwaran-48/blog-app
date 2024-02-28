package com.vicky.blog.common.service;

import java.util.List;

import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.exception.AppException;

public interface FollowService {

    void followProfile(String userId, String profileId) throws AppException;

    List<FollowDTO> getFollowersOfProfile(String userId, String profileId) throws AppException;
    
}