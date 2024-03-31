package com.vicky.blog.controller;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app/test")
public class TestController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @PostMapping("/dummy-blogs/{userId}")
    public ResponseEntity<EmptyResponse> populateDummpBlogs(@PathVariable String userId, HttpServletRequest request)
            throws AppException {
        UserDTO userDTO = userService.getUser(userId).get();

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent("Testing blog content");
        blogDTO.setImage("http://localhost:7000/static/6606dbbf2a7a8a3740aea780");
        blogDTO.setOwner(userDTO);

        for (int i = 0; i < 20; i++) {
            blogDTO.setTitle("Test blog " + i);
            String blogId = blogService.addBlog(userId, blogDTO);
            blogService.publishBlog(userId, blogId, userDTO.getProfileId());
        }

        EmptyResponse response = new EmptyResponse();
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
}
