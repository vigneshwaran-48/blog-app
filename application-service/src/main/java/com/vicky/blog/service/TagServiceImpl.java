package com.vicky.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.tag.TagDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.BlogTag;
import com.vicky.blog.model.Tag;
import com.vicky.blog.model.TagFollow;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.BlogTagRepoistory;
import com.vicky.blog.repository.mongo.TagFollowRepository;
import com.vicky.blog.repository.mongo.TagRepository;

@Service
public class TagServiceImpl implements TagService {
    
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogTagRepoistory blogTagRepoistory;

    @Autowired
    private TagFollowRepository tagFollowRepository;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void applyTagToBlog(String userId, String blogId, String tagId) throws AppException {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Given tag id not exists!");
        }
        BlogDTO blog = blogService.getBlog(userId, blogId).get();
        BlogTag blogTag = new BlogTag();
        blogTag.setBlog(Blog.build(blog));
        blogTag.setTag(tagOptional.get());
        BlogTag appliedTag = blogTagRepoistory.save(blogTag);
        if (appliedTag == null) {
            throw new AppException("Error while adding tag to the blog!");
        }
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public List<TagDTO> getTagsOfBlog(String userId, String blogId) throws AppException {
        List<BlogTag> blogTags = blogTagRepoistory.findByBlogId(blogId);
        return blogTags.stream()
                        .map(blogTag -> blogTag.getTag().toDTO())
                        .collect(Collectors.toList());
    }

    @Override
    public String addTag(String tagName) throws AppException {
        Tag tag = new Tag();
        tag.setName(tagName);
        Tag savedtag = tagRepository.save(tag);
        if (savedtag == null) {
            throw new AppException("Error while adding tag!");
        }
        return savedtag.getId();
    }

    @Override
    public List<TagDTO> getAllTags() throws AppException {
        return tagRepository.findAll()
                            .stream()
                            .map(tag -> tag.toDTO())
                            .collect(Collectors.toList());
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void removeTagFromBlog(String userId, String blogId, String tagId) throws AppException {
        blogTagRepoistory.deleteByBlogIdAndTagId(blogId, tagId);
    }

    @Override
    @UserIdValidator(positions = 0)
    public void followTag(String userId, String tagId) throws AppException {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Given tag id not exists!");
        }
        UserDTO user = userService.getUser(userId).get();
        TagFollow tagFollow = new TagFollow();
        tagFollow.setFollower(User.build(user));
        tagFollow.setTag(tagOptional.get());
        TagFollow savedtagFollow = tagFollowRepository.save(tagFollow);
        if (savedtagFollow == null) {
            throw new AppException("Error while following tag!");
        }
    }

    @Override
    @UserIdValidator(positions = 0)
    public void unFollowTag(String userId, String tagId) throws AppException {
        tagFollowRepository.deleteByFollowerIdAndTagId(userId, tagId);
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<TagDTO> getFollowingTags(String userId) throws AppException {
        return tagFollowRepository.findByFollowerId(userId)
                        .stream()
                        .map(tagFollow -> tagFollow.getTag().toDTO())
                        .collect(Collectors.toList());
    }
}
