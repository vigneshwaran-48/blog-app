package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.blog.BlogViewStats;
import com.vicky.blog.common.dto.blog.BlogsViewStatsResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogViewService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/blog/stats")
public class BlogsStatsController {

    @Autowired
    private UserIdExtracter userIdExtracter;

    @Autowired
    private BlogViewService blogViewService;

    @GetMapping("/view")
    public ResponseEntity<BlogsViewStatsResponse> getAllBlogsViewStats(Principal principal, HttpServletRequest request)
            throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        List<BlogViewStats> blogViewStats = blogViewService.getAllBlogViewStatsOfUser(userId);

        BlogsViewStatsResponse response = new BlogsViewStatsResponse();
        response.setMessage("success");
        response.setStats(blogViewStats);
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }
}
