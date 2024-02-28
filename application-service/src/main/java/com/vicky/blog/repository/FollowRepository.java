package com.vicky.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    List<Follow> findByUserProfileProfileId(String profileId);
}
