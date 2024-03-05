package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.comment.CommentCreationPayload;
import com.vicky.blog.common.dto.comment.CommentDTO;
import com.vicky.blog.common.dto.comment.CommentResponse;
import com.vicky.blog.common.dto.comment.CommentsResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.CommentService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/blog/{blogId}/comment")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserIdExtracter userIdExtracter;

    @PostMapping
    public ResponseEntity<CommentResponse> commentInBlog(@PathVariable Long blogId, 
        @RequestBody CommentCreationPayload payload, Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        CommentDTO comment = commentService.addComment(userId, blogId, payload.getParentCommentId(), payload.getContent());
        CommentResponse response = new CommentResponse();
        response.setComment(comment);
        response.setMessage("Commented in Blog!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommentsResponse> getCommentsOfBlog(@PathVariable Long blogId, Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<CommentDTO> comments = commentService.getCommentsOfBlog(userId, blogId);

        CommentsResponse response = new CommentsResponse();
        response.setTime(LocalDateTime.now());
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setComments(comments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long blogId, @PathVariable Long commentId, 
        Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        Optional<CommentDTO> comment = commentService.getComment(userId, blogId, commentId);

        CommentResponse response = new CommentResponse();
        response.setComment(comment.isPresent() ? comment.get() : null);
        response.setMessage("success");
        response.setStatus(comment.isPresent() ? HttpStatus.SC_OK : HttpStatus.SC_NOT_FOUND);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<EmptyResponse> deleteComment(@PathVariable Long blogId, @PathVariable Long commentId, 
        Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        commentService.deleteCommentWithItsThreads(userId, blogId, commentId);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Deleted Comment!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{commentId}/like")
    public ResponseEntity<EmptyResponse> likeComment(@PathVariable Long blogId, @PathVariable Long commentId, 
    Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        commentService.likeComment(userId, blogId, commentId);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Liked Comment!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<EmptyResponse> unLikeComment(@PathVariable Long blogId, @PathVariable Long commentId, 
    Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        commentService.removeLike(userId, blogId, commentId);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("UnLiked Comment!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}
