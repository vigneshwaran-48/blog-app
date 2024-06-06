package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.BlogTag;

import jakarta.transaction.Transactional;

@Repository
public interface BlogTagRepoistory extends MongoRepository<BlogTag, String> {
    
    List<BlogTag> findByBlogId(String blogId);

    List<BlogTag> findByTagId(String tagId);

    Optional<BlogTag> findByBlogIdAndTagId(String blogId, String tagId);

    @Transactional
    void deleteByBlogIdAndTagId(String blogId, String tagId);

    @Transactional
    void deleteByBlogId(String blogId);

    Page<BlogTag> findByTagId(String tagId, Pageable pageable);
}