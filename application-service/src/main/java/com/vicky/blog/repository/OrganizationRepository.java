package com.vicky.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    
}
