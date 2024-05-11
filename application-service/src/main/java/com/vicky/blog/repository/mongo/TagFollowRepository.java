package com.vicky.blog.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.TagFollow;

import jakarta.transaction.Transactional;

@Repository
public interface TagFollowRepository extends MongoRepository<TagFollow, String> {
    
    @Transactional
    void deleteByFollowerIdAndTagId(String followerId, String tagId);

    List<TagFollow> findByFollowerId(String followerId);
}
