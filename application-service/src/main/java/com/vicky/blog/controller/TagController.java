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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogsResponse;
import com.vicky.blog.common.dto.tag.TagDTO;
import com.vicky.blog.common.dto.tag.TagResponse;
import com.vicky.blog.common.dto.tag.TagsResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/tag")
public class TagController {

    @Autowired
    private UserIdExtracter userIdExtracter;

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity<EmptyResponse> addTag(@RequestParam String name, @RequestParam String description,
            Principal principal, HttpServletRequest request) throws AppException {
        String tagId = tagService.addTag(name, description);
        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Added tag, Tag Id is " + tagId);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<TagsResponse> getAllTags(Principal principal, HttpServletRequest request)
            throws AppException {
        List<TagDTO> tags = tagService.getAllTags();

        TagsResponse response = new TagsResponse();
        response.setTags(tags);
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponse> getTag(@PathVariable String tagId, Principal principal, HttpServletRequest request)
            throws AppException {
        Optional<TagDTO> tag = tagService.getTag(tagId);

        if (tag.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Invalid tag id!");
        }

        TagResponse response = new TagResponse();
        response.setTag(tag.get());
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{tagId}/blog/{blogId}")
    public ResponseEntity<EmptyResponse> applyTag(@PathVariable String tagId, @PathVariable String blogId,
            Principal principal, HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        tagService.applyTagToBlog(userId, blogId, tagId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Applied tag to the blog");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<TagsResponse> getTagsOfBlog(@PathVariable String blogId, Principal principal,
            HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        List<TagDTO> tags = tagService.getTagsOfBlog(userId, blogId);

        TagsResponse response = new TagsResponse();
        response.setTags(tags);
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{tagId}/blog")
    public ResponseEntity<BlogsResponse> getBlogsOfTag(@PathVariable String tagId, Principal principal,
            HttpServletRequest request) throws AppException {
        List<BlogDTO> blogs = tagService.getAllBlogsOfTag(tagId);

        BlogsResponse response = new BlogsResponse();
        response.setBlogs(blogs);
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{tagId}/blog/{blogId}")
    public ResponseEntity<EmptyResponse> removeTagFromBlog(@PathVariable String tagId, @PathVariable String blogId,
            Principal principal, HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        tagService.removeTagFromBlog(userId, blogId, tagId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Removed tag from the blog!");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{tagId}/follow")
    public ResponseEntity<EmptyResponse> followTag(@PathVariable String tagId, Principal principal,
            HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        tagService.followTag(userId, tagId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Followed the tag!");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{tagId}/follow")
    public ResponseEntity<EmptyResponse> unFollowTag(@PathVariable String tagId, Principal principal,
            HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        tagService.unFollowTag(userId, tagId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("UnFollowed the tag!");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/follow")
    public ResponseEntity<TagsResponse> getFollowingTags(Principal principal, HttpServletRequest request)
            throws AppException {
        String userId = userIdExtracter.getUserId(principal);
        List<TagDTO> followingTags = tagService.getFollowingTags(userId);

        TagsResponse response = new TagsResponse();
        response.setTags(followingTags);
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
}
