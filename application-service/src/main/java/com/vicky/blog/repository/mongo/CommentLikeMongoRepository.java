package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.CommentLike;

import jakarta.transaction.Transactional;

public interface CommentLikeMongoRepository extends MongoRepository<CommentLike, String> {
    
    List<CommentLike> findByCommentId(String commentId);

    Optional<CommentLike> findByCommentIdAndLikedById(String commentId, String userId);

    @Transactional
    void deleteByCommentIdAndLikedById(String commentId, String userId);

    boolean existsByCommentIdAndLikedById(String commentId, String userId);
}
