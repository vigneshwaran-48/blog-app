package com.vicky.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByParentCommentId(Long id);

    Optional<Comment> findByIdAndBlogId(Long id, Long blogId);

    List<Comment> findByBlogIdAndParentCommentIdIsNull(Long blogId);
}
