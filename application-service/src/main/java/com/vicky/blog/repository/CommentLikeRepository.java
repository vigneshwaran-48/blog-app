package com.vicky.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.CommentLike;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    
    @Query("SELECT COUNT(cl.id) FROM CommentLike as cl")
    int getLikesCountOfComment(Long commentId);
}
