package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogFeedsDTO;
import com.vicky.blog.common.dto.tag.TagDTO;
import com.vicky.blog.common.exception.AppException;

public interface TagService {
    
    void applyTagToBlog(String userId, String blogId, String tagId) throws AppException;

    void applyTagsToBlog(String userId, String blogId, List<String> tagIds) throws AppException;
    
    List<TagDTO> getTagsOfBlog(String userId, String blogId) throws AppException;

    String addTag(String tagName, String description) throws AppException;

    List<TagDTO> getAllTags() throws AppException;

    void removeTagFromBlog(String userId, String blogId, String tagId) throws AppException;

    void followTag(String userId, String tagId) throws AppException;

    void unFollowTag(String userId, String tagId) throws AppException;

    List<TagDTO> getFollowingTags(String userId) throws AppException;

    Optional<TagDTO> getTagByName(String name) throws AppException;

    Optional<TagDTO> getTag(String id) throws AppException;

    List<BlogDTO> getAllBlogsOfTag(String tagId) throws AppException;

    BlogFeedsDTO getBlogsOfTagForFeeds(String userId, String tagId, int page, int size) throws AppException;

}
