package com.vicky.blog.service.blog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogAccessTracker;
import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.PostProcess;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.blog.BlogFeedsDTO;
import com.vicky.blog.common.dto.blog.BlogFeedsDTO.PageStatus;
import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserDTO.UserType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.exception.OrganizationNotAccessible;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.NotificationService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.common.utility.PostProcessType;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.repository.mongo.BlogMongoRepository;

@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogMongoRepository blogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private BlogUtil blogUtil;

    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TagService tagService;

    @Override
    @UserIdValidator(positions = 0)
    public String addBlog(String userId, BlogDTO blogDTO) throws AppException {

        UserDTO user = userService.getUser(userId).get();
        blogDTO.setOwner(user);
        blogDTO.setDescription(blogUtil.getDescriptionForBlog(blogDTO.getContent()));
        blogUtil.validateAndFormatBlogData(blogDTO);

        blogDTO.setPostedTime(LocalDateTime.now());

        Blog blog = Blog.build(blogDTO);
        Blog savedBlog = blogRepository.save(blog);

        if (savedBlog == null) {
            LOGGER.error("Error while adding blog {}", blog);
            throw new AppException("Errow while adding blog!");
        }
        return savedBlog.getId();
    }

    @Override
    @BlogAccessTracker(userIdPosition = 0)
    public Optional<BlogDTO> getBlog(String userId, String id) throws AppException {

        Optional<Blog> blog = blogRepository.findById(id);

        if (blog.isEmpty()) {
            return Optional.empty();
        }
        BlogDTO blogDTO = blog.get().toDTO();

        boolean isBlogOwnerNotTheUserAndBlogNotPublished =
                !blogDTO.getOwner().getId().equals(userId) && !blogDTO.isPublised();
        if (isBlogOwnerNotTheUserAndBlogNotPublished) {
            LOGGER.info("Blog {} owner is not the user {} and it is not published!", id, userId);
            return Optional.empty();
        }
        String ownerProfileId = profileIdService.getProfileIdByEntityId(blogDTO.getOwner().getId()).get();
        blogDTO.getOwner().setProfileId(ownerProfileId);

        blogDTO.setDisplayPostedDate(blogUtil.getDisplayPostedData(blogDTO.getPostedTime()));
        blogDTO.setTags(tagService.getTagsOfBlog(userId, id));

        return Optional.of(blogDTO);
    }

    @Override
    @UserIdValidator(positions = 0)
    public Optional<BlogDTO> updateBlog(String userId, BlogDTO blogDTO) throws AppException {

        Optional<BlogDTO> existingBlog = getBlog(userId, blogDTO.getId());

        if (existingBlog.isEmpty()) {
            LOGGER.error("Blog {} not exists", blogDTO.getId());
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Blog not exists");
        }

        blogUtil.checkAndFillMissingDataForPatchUpdate(blogDTO, existingBlog.get());
        blogUtil.validateAndFormatBlogData(blogDTO);

        Blog savedBlog = blogRepository.save(Blog.build(blogDTO));

        if (savedBlog == null) {
            LOGGER.error("Error while saving blog {}", blogDTO);
            throw new AppException("Errow while saving blog!");
        }
        return Optional.of(savedBlog.toDTO());
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void deleteBlog(String userId, String id) throws AppException {
        blogRepository.deleteByOwnerIdAndId(userId, id);
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<BlogDTO> getAllBlogsOfUser(String userId) throws AppException {
        List<Blog> blogs = blogRepository.findByOwnerId(userId);

        if (blogs.isEmpty()) {
            return List.of();
        }
        List<BlogDTO> blogDTOs = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogDTO blogDTO = blog.toDTO();
            blogDTO.setDisplayPostedDate(blogUtil.getDisplayPostedData(blogDTO.getPostedTime()));
            String ownerProfileId = profileIdService.getProfileIdByEntityId(blogDTO.getOwner().getId()).get();
            blogDTO.getOwner().setProfileId(ownerProfileId);

            blogDTOs.add(blogDTO);
        }
        return blogDTOs;
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void publishBlog(String userId, String blogId, String publishAt) throws AppException {
        UserDTO user = userService.getUser(userId).get();
        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(publishAt)
                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "Profile Id not exists"));
        BlogDTO blogDTO = getBlog(userId, blogId).get();
        OrganizationDTO organizationDTO = null;

        if (profileIdDTO.getType() == ProfileType.USER) {
            if (!user.getId().equals(profileIdDTO.getEntityId())) {
                LOGGER.error("User {} tried to publish as {}", userId, profileIdDTO.getEntityId());
                throw new AppException(HttpStatus.SC_FORBIDDEN, "You can't publish as other user!");
            }
        } else {
            // Validating does user can access organization.
            organizationDTO = organizationService.getOrganization(userId, profileIdDTO.getEntityId()).get();
        }

        blogDTO.setPublised(true);
        blogDTO.setPublishedAt(profileIdDTO);
        updateBlog(userId, blogDTO);

        LOGGER.info("Published blog at {}", profileIdDTO.getProfileId());

        List<FollowDTO> followers = followService.getFollowersOfProfile(userId, profileIdDTO.getProfileId());
        NotificationDTO notification = new NotificationDTO();
        notification.setSenderType(NotificationSenderType.USER);
        notification.setMessage(user.getName() + " has published a blog");

        if (profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            notification.setSenderType(NotificationSenderType.ORGANIZATION);
            notification.setOrganizationId(organizationDTO.getId());
            notification.setMessage(organizationDTO.getName() + " has published a blog");
        }
        for (FollowDTO follower : followers) {
            notification.setUserId(follower.getFollower().getEntityId());
            notificationService.addNotification(userId, notification);
        }
        LOGGER.info("Notified the profile {} followers", profileIdDTO.getProfileId());
    }

    @Override
    @BlogAccessTracker(userIdPosition = 0)
    public Optional<BlogDTO> getBlogOfProfile(String userId, String blogId, String profileId) throws AppException {

        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId)
                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "Profile Id not exists"));

        if (profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Validating does user can access organization.
            organizationService.getOrganization(userId, profileIdDTO.getEntityId()).get();
        }
        Optional<Blog> blog = blogRepository.findByIdAndPublishedAtId(blogId, profileIdDTO.getId());
        if (blog.isEmpty()) {
            // If the user wants to see his own blog with his profileId
            // But Not getting userId from profileDTO because it will lead to anyone can
            // access the blog with a user's profileId.
            blog = blogRepository.findByOwnerIdAndId(userId, blogId);
            if (blog.isEmpty()) {
                return Optional.empty();
            }
        }
        BlogDTO blogDTO = blog.get().toDTO();
        blogDTO.setDisplayPostedDate(blogUtil.getDisplayPostedData(blogDTO.getPostedTime()));
        blogDTO.setTags(tagService.getTagsOfBlog(userId, blogId));
        return Optional.of(blogDTO);
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<BlogDTO> getAllBlogsOfProfile(String userId, String profileId) throws AppException {
        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId)
                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "Profile Id not exists"));

        if (profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Validating does user can access organization.
            organizationService.getOrganization(userId, profileIdDTO.getEntityId()).get();
        }
        List<Blog> blogs = blogRepository.findByPublishedAtId(profileIdDTO.getId());
        return blogs.stream().map(blog -> blog.toDTO()).collect(Collectors.toList());
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void unPublishBlog(String userId, String blogId) throws AppException {
        BlogDTO blogDTO =
                getAllBlogsOfUser(userId).stream().filter(blog -> blog.getId().equals(blogId)).findFirst().get();

        blogDTO.setPublised(false);
        blogDTO.setPublishedAt(null);
        updateBlog(userId, blogDTO);
        LOGGER.info("UnPublished blog {}", blogId);
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<BlogDTO> getAllBlogsVisibleToUser(String userId) throws AppException {
        List<Blog> blogs = blogRepository.findAll();
        List<BlogDTO> blogsUserHasAcces = new LinkedList<>();
        for (Blog blog : blogs) {
            ProfileId publishedProfile = blog.getPublishedAt();
            if (publishedProfile == null) {
                continue;
            }
            if (publishedProfile.getType() == ProfileType.ORGANIZATION) {
                try {
                    organizationService.getOrganization(userId, publishedProfile.getEntityId());
                } catch (OrganizationNotAccessible e) {
                    // Ignoring
                    // If this happens then the user not have access to the organization
                    // publishin blogs.
                    LOGGER.info("User {} not have access to the blog {}", userId, blog.getId());
                    continue;
                }
            }
            blogsUserHasAcces.add(blog.toDTO());
        }
        return blogsUserHasAcces;
    }

    @Override
    public BlogFeedsDTO getBlogsForUserFeed(String userId, int page, int size) throws AppException {
        UserType userType = userService.getUserType(userId);
        if (userType == UserType.GUEST && page > 0) {
            return new BlogFeedsDTO(PageStatus.SIGNUP, List.of());
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogRepository.findByOwnerIdNotAndIsPublised(userId, true, pageable);
        List<BlogDTO> blogsForFeed = new ArrayList<>();
        for (Blog blog : blogs) {
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
            blogDTO.setTags(tagService.getTagsOfBlog(userId, blogDTO.getId()));
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

    @Override
    public BlogFeedsDTO getBlogsOfFollowingUsers(String userId, int page, int limit) throws AppException {
        List<UserDTO> followingUsers = followService.getAllFollowingUsers(userId);
        BlogFeedsDTO feedsDTO = new BlogFeedsDTO();
        for (UserDTO userDTO : followingUsers) {
            List<BlogDTO> blogs = blogUtil.getPostedBlogsOfUser(userDTO.getId(), page, limit);
            if (feedsDTO.getFeeds() == null) {
                feedsDTO.setFeeds(blogs);
                continue;
            }
            feedsDTO.getFeeds().addAll(blogs);
        }
        if (feedsDTO.getFeeds() == null || feedsDTO.getFeeds().size() < limit) {
            feedsDTO.setStatus(PageStatus.NOT_AVAILABLE);
        } else {
            feedsDTO.setStatus(PageStatus.AVAILABLE);
        }
        return feedsDTO;
    }
}
