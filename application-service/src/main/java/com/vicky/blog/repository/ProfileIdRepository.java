package com.vicky.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.ProfileId;

@Repository
public interface ProfileIdRepository extends JpaRepository<ProfileId, Long> {
    
    Optional<ProfileId> findByEntityId(String entityId);

    boolean existsByProfileId(String profileId);

    Optional<ProfileId> findByProfileId(String profileId);
}
