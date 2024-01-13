package com.vicky.blog.staticservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.staticservice.model.StaticResource;

@Repository
public interface StaticResourceRepository extends JpaRepository<StaticResource, Long> {
    
}
