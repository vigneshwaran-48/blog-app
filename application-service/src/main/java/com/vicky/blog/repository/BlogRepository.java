package com.vicky.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    
}