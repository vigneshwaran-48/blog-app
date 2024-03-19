package com.vicky.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.BlogLike;

import jakarta.transaction.Transactional;

@Repository
public interface BlogLikeRepository extends JpaRepository<BlogLike, Long> {
    
    @Transactional
    void deleteByBlogIdAndLikedById(Long blogId, String userId);

    List<BlogLike> findByBlogId(Long blogId);

    Optional<BlogLike> findByBlogIdAndLikedById(Long blogId, String userId);
}