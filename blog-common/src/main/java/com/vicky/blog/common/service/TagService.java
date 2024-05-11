package com.vicky.blog.common.service;

import java.util.List;

import com.vicky.blog.common.dto.tag.TagDTO;
import com.vicky.blog.common.exception.AppException;

public interface TagService {
    
    void applyTagToBlog(String userId, String blogId, String tagId) throws AppException;
    
    List<TagDTO> getTagsOfBlog(String userId, String blogId) throws AppException;

    String addTag(String tagName, String description) throws AppException;

    List<TagDTO> getAllTags() throws AppException;

    void removeTagFromBlog(String userId, String blogId, String tagId) throws AppException;

    void followTag(String userId, String tagId) throws AppException;

    void unFollowTag(String userId, String tagId) throws AppException;

    List<TagDTO> getFollowingTags(String userId) throws AppException;

}
