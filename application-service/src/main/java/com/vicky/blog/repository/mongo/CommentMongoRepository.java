package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.model.Comment;

public interface CommentMongoRepository extends MongoRepository<Comment, String> {
    
    List<Comment> findByParentCommentId(String id);

    Optional<Comment> findByIdAndBlogId(String id, String blogId);

    List<Comment> findByBlogIdAndParentCommentIdIsNull(String blogId);
}
