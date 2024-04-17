package com.vicky.blog.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Follow;

public interface FollowMongoRepository extends MongoRepository<Follow, String> {
    
    List<Follow> findByUserProfileId(String id);

    void deleteByUserProfileIdAndFollowerId(String profileId, String followerId);

    List<Follow> findByFollowerId(String followerId);

}
