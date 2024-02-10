package com.vicky.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByUserIdAndId(String userId, Long id);

    void deleteByUserIdAndId(String userId, Long id);
}