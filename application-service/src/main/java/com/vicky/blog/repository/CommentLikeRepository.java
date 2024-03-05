package com.vicky.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.CommentLike;

import jakarta.transaction.Transactional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    
    List<CommentLike> findByCommentId(Long commentId);

    Optional<CommentLike> findByCommentIdAndLikedById(Long commentId, String userId);

    @Transactional
    void deleteByCommentIdAndLikedById(Long commentId, String userId);
}
