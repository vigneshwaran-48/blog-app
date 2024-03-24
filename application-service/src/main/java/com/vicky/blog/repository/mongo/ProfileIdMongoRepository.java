package com.vicky.blog.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.ProfileId;

public interface ProfileIdMongoRepository extends MongoRepository<ProfileId, String> {
    
    Optional<ProfileId> findByEntityId(String entityId);

    boolean existsByProfileId(String profileId);

    Optional<ProfileId> findByProfileId(String profileId);

    void deleteByProfileIdAndEntityId(String profileId, String entityId);
}
