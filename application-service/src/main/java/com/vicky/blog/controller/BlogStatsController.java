package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.blog.BlogReadDTO;
import com.vicky.blog.common.dto.blog.BlogReadStatsResponse;
import com.vicky.blog.common.dto.blog.BlogViewStats;
import com.vicky.blog.common.dto.blog.BlogViewStatsResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogReadService;
import com.vicky.blog.common.service.BlogViewService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/app/blog/{blogId}/stats")
@RestController
public class BlogStatsController {

    @Autowired
    private UserIdExtracter userIdExtracter;

    @Autowired
    private BlogReadService blogReadService;

    @Autowired
    private BlogViewService blogViewService;

    @PostMapping("/read")
    public ResponseEntity<EmptyResponse> setBlogReadTime(@PathVariable String blogId, @RequestParam long readTime,
            Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        blogReadService.setReadTime(userId, blogId, readTime);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Setted read time!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/read")
    public ResponseEntity<BlogReadStatsResponse> getBlogReadStats(@PathVariable String blogId, Principal principal,
            HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<BlogReadDTO> blogReads = blogReadService.getReadStatsOfBlog(userId, blogId);
        BlogReadStatsResponse response = new BlogReadStatsResponse();
        response.setBlogReads(blogReads);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/view")
    public ResponseEntity<EmptyResponse> setBlogView(@PathVariable String blogId, Principal principal,
            HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        blogViewService.addBlogView(userId, blogId);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Setted blog view!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/view")
    public ResponseEntity<BlogViewStatsResponse> getBlogViewStats(@PathVariable String blogId, Principal principal,
            HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        BlogViewStats blogViewStats = blogViewService.getBlogViewStats(userId, blogId);

        BlogViewStatsResponse response = new BlogViewStatsResponse();
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setStats(blogViewStats);
        response.setTime(LocalDateTime.now());
        response.setStatus(HttpStatus.SC_OK);
        return ResponseEntity.ok().body(response);
    }
}
