package com.vicky.blog.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.OrganizationUser;

public interface OrganizationUserMongoRepository extends MongoRepository<OrganizationUser, Long> {
    
}
