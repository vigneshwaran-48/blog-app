package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Organization;

public interface OrganizationMongoRepository extends MongoRepository<Organization, String> {
    
}
