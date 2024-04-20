package com.vicky.blog.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.BlogTag;

import jakarta.transaction.Transactional;

@Repository
public interface BlogTagRepoistory extends MongoRepository<BlogTag, String> {
    
    List<BlogTag> findByBlogId(String blogId);

    @Transactional
    void deleteByBlogIdAndTagId(String blogId, String tagId);
}