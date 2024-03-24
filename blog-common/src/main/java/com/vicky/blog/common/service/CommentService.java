package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.comment.CommentDTO;
import com.vicky.blog.common.exception.AppException;

public interface CommentService {
    
    CommentDTO addComment(String userId, String blogId, String parentCommentId, String commentContent) throws AppException;

    CommentDTO editComment(String userId, String blogId, String commentId, String commentContent) throws AppException;

    void deleteComment(String userId, String blogId, String commentId) throws AppException;

    void deleteCommentWithItsThreads(String userId, String blogId, String commentId) throws AppException;
    
    Optional<CommentDTO> getComment(String userId, String blogId, String commentId) throws AppException;

    List<CommentDTO> getThreadsOfComment(String userId, String blogId, String commentId) throws AppException;

    List<CommentDTO> getCommentsOfBlog(String userId, String blogId) throws AppException;

    List<CommentDTO> getImmediateNextChildThreadsOfComment(String userId, String blogId, String commentId) 
        throws AppException;

    void likeComment(String userId, String blogId, String commentId) throws AppException;

    void removeLike(String userId, String blogId, String commentId) throws AppException;
}
