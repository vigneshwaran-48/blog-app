package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.comment.CommentDTO;
import com.vicky.blog.common.exception.AppException;

public interface CommentService {
    
    CommentDTO addComment(String userId, Long blogId, Long parentCommentId, String commentContent) throws AppException;

    CommentDTO editComment(String userId, Long blogId, Long commentId, String commentContent) throws AppException;

    void deleteComment(String userId, Long blogId, Long commentId) throws AppException;

    void deleteCommentWithItsThreads(String userId, Long blogId, Long commentId) throws AppException;
    
    Optional<CommentDTO> getComment(String userId, Long blogId, Long commentId) throws AppException;

    List<CommentDTO> getThreadsOfComment(String userId, Long blogId, Long commentId) throws AppException;

    List<CommentDTO> getCommentsOfBlog(String userId, Long blogId) throws AppException;

    List<CommentDTO> getImmediateNextChildThreadsOfComment(String userId, Long blogId, Long commentId) 
        throws AppException;
}
