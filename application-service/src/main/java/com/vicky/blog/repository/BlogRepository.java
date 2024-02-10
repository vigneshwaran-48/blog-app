package com.vicky.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Blog;

import jakarta.transaction.Transactional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByOwnerIdAndId(String userId, Long id);

    @Transactional
    void deleteByOwnerIdAndId(String userId, Long id);
}