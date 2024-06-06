package com.vicky.blog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogFeedsDTO;
import com.vicky.blog.common.dto.blog.BlogFeedsDTO.PageStatus;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.tag.TagDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.exception.OrganizationNotAccessible;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.BlogTag;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.model.Tag;
import com.vicky.blog.model.TagFollow;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.BlogTagRepoistory;
import com.vicky.blog.repository.mongo.TagFollowRepository;
import com.vicky.blog.repository.mongo.TagRepository;
import com.vicky.blog.service.blog.BlogUtil;

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

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private BlogUtil blogUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void applyTagToBlog(String userId, String blogId, String tagId) throws AppException {
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Given tag id not exists!");
        }
        Optional<BlogTag> existingBlogTag = blogTagRepoistory.findByBlogIdAndTagId(blogId, tagId);
        if (existingBlogTag.isPresent()) {
            LOGGER.info("Tag {} has already been tagged to the blog {}", tagId, blogId);
            return;
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
    public List<TagDTO> getTagsOfBlog(String userId, String blogId) throws AppException {
        List<BlogTag> blogTags = blogTagRepoistory.findByBlogId(blogId);
        return blogTags.stream().map(blogTag -> blogTag.getTag().toDTO()).collect(Collectors.toList());
    }

    @Override
    public String addTag(String tagName, String description) throws AppException {
        if (tagRepository.findByName(tagName).isPresent()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Tag name already exists!");
        }
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setDescription(description);
        Tag savedtag = tagRepository.save(tag);
        if (savedtag == null) {
            throw new AppException("Error while adding tag!");
        }
        return savedtag.getId();
    }

    @Override
    public List<TagDTO> getAllTags() throws AppException {
        return tagRepository.findAll().stream().map(tag -> tag.toDTO()).collect(Collectors.toList());
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
        return tagFollowRepository.findByFollowerId(userId).stream().map(tagFollow -> tagFollow.getTag().toDTO())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDTO> getTagByName(String name) throws AppException {
        Optional<Tag> tag = tagRepository.findByName(name);
        if (tag.isPresent()) {
            return Optional.of(tag.get().toDTO());
        }
        return Optional.empty();
    }

    @Override
    public Optional<TagDTO> getTag(String id) throws AppException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return Optional.of(tag.get().toDTO());
        }
        return Optional.empty();
    }

    @Override
    public List<BlogDTO> getAllBlogsOfTag(String tagId) throws AppException {
        return blogTagRepoistory.findByTagId(tagId).stream().map(blogTag -> blogTag.getBlog().toDTO())
                .collect(Collectors.toList());
    }

    @Override
    public void applyTagsToBlog(String userId, String blogId, List<String> tagIds) throws AppException {
        blogTagRepoistory.deleteByBlogId(blogId);
        for (String tagId : tagIds) {
            applyTagToBlog(userId, blogId, tagId);
        }
    }

    @Override
    public BlogFeedsDTO getBlogsOfTagForFeeds(String userId, String tagId, int page, int size) throws AppException {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogTag> blogs = blogTagRepoistory.findByTagId(tagId, pageable);
        List<BlogDTO> blogsForFeed = new ArrayList<>();
        for (BlogTag blogTag : blogs) {
            Blog blog = blogTag.getBlog();
            ProfileId profileId = blog.getPublishedAt();
            if (profileId.getType() == ProfileType.ORGANIZATION) {
                try {
                    organizationService.getOrganization(userId, profileId.getEntityId());
                } catch (OrganizationNotAccessible e) {
                    // Ignoring
                    // If this happens then the user not have access to the organization
                    // publishin blogs.
                    LOGGER.info("User {} not have access to the blog {}", userId, blog.getId());
                    continue;
                }
            }
            BlogDTO blogDTO = blog.toDTO();
            blogDTO.setDisplayPostedDate(blogUtil.getDisplayPostedData(blogDTO.getPostedTime()));
            blogDTO.setTags(getTagsOfBlog(userId, blogDTO.getId()));
            blogsForFeed.add(blogDTO);
        }
        BlogFeedsDTO feedsDTO = new BlogFeedsDTO();
        feedsDTO.setFeeds(blogsForFeed);
        if (blogsForFeed.size() < size) {
            feedsDTO.setStatus(PageStatus.NOT_AVAILABLE);
        } else {
            feedsDTO.setStatus(PageStatus.AVAILABLE);
        }
        return feedsDTO;
    }
}
