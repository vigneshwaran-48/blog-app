package com.vicky.blog.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Preference;

@Repository
public interface PreferenceRepository extends MongoRepository<Preference, String> {
    
    Optional<Preference> findByUserId(String userId);
}
