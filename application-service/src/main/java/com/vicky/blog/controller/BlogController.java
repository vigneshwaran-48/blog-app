package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogResponse;
import com.vicky.blog.common.dto.blog.BlogsResponse;
import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;
import com.vicky.blog.common.dto.bloglike.BlogLikesCountResponse;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogLikeService;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserIdExtracter userIdExtracter;
    @Autowired
    private BlogLikeService blogLikeService;
    
    @PostMapping
    public ResponseEntity<?> addBlog(@RequestBody BlogDTO blogDTO, HttpServletRequest request, 
        Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        blogDTO.setOwner(userDTO);

        Long id = blogService.addBlog(blogDTO);
        BlogDTO blog = blogService.getBlog(userId, id).get();

        BlogResponse response = new BlogResponse();
        response.setBlog(blog);
        response.setMessage("Added blog!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBlogsOfUser(@PathVariable String userId, HttpServletRequest request) throws AppException {
        
        // Everyone can see everyones blog for now. Should be adding some restriction in furture.
        List<BlogDTO> blogs = blogService.getAllBlogsOfUser(userId);

        BlogsResponse response = new BlogsResponse();
        response.setBlogs(blogs);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlog(@PathVariable Long id, HttpServletRequest request, 
        Principal principal) throws AppException {
        
        String userId = userIdExtracter.getUserId(principal);

        Optional<BlogDTO> blog = blogService.getBlog(userId, id);

        BlogResponse response = new BlogResponse();
        response.setBlog(blog.isPresent() ? blog.get() : null);
        response.setMessage(blog.isPresent() ? "success" : "Blog not exists!");
        response.setPath(request.getServletPath());
        response.setStatus(blog.isPresent() ? HttpStatus.SC_OK : HttpStatus.SC_NO_CONTENT);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping
    public ResponseEntity<?> updateBlog(@RequestBody BlogDTO blogDTO, HttpServletRequest request, Principal principal) 
        throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        blogDTO.setOwner(userDTO);

        BlogDTO savedBlog = blogService.updateBlog(blogDTO).get();

        BlogResponse response = new BlogResponse();
        response.setBlog(savedBlog);
        response.setMessage("Updated blog!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id, Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        blogService.deleteBlog(userId, id);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Deleted blog!");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{blogId}/like")
    public ResponseEntity<?> likeBlog(@PathVariable Long blogId, Principal principal, HttpServletRequest request) 
        throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        blogLikeService.addLike(blogId, userId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Liked Blog!");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{blogId}/like")
    public ResponseEntity<?> unLikeBlog(@PathVariable Long blogId, Principal principal, HttpServletRequest request) 
        throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        blogLikeService.removeLike(blogId, userId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("UnLiked Blog!");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{blogId}/like/count")
    public ResponseEntity<?> getLikesCountOfBlog(@PathVariable Long blogId, Principal principal, HttpServletRequest request) 
        throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<BlogLikeDTO> blogLikes = blogLikeService.getLikesOfBlog(userId, blogId);

        BlogLikesCountResponse response = new BlogLikesCountResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Liked Blog!");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setLikesCount(blogLikes.size());
        
        return ResponseEntity.ok().body(response);
    }
}
