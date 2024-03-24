package com.vicky.blog.staticservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.staticservice.model.StaticResource;

@Repository
public interface StaticResourceRepository extends MongoRepository<StaticResource, String> {
    
}
