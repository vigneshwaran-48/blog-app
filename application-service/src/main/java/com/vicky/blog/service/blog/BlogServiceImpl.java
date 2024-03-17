package com.vicky.blog.service.blog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.NotificationService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.repository.BlogRepository;

@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogRepository blogRepository;

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

    @Override
    @UserIdValidator(positions = 0)
    public Long addBlog(String userId, BlogDTO blogDTO) throws AppException {

        UserDTO user = userService.getUser(userId).get();
        blogDTO.setOwner(user);
        blogDTO.setDescription(blogUtil.getDescriptionForBlog(blogDTO.getContent()));
        blogUtil.validateAndFormatBlogData(blogDTO);

        blogDTO.setPostedTime(LocalDateTime.now());

        Blog blog = Blog.build(blogDTO);
        Blog savedBlog = blogRepository.save(blog);

        if(savedBlog == null) {
            LOGGER.error("Error while adding blog {}", blog);
            throw new AppException("Errow while adding blog!");
        }
        return savedBlog.getId();
    }

    @Override
    @UserIdValidator(positions = 0)
    public Optional<BlogDTO> getBlog(String userId, Long id) throws AppException {
        
        Optional<Blog> blog = blogRepository.findById(id);

        if(blog.isEmpty()) {
            return Optional.empty();
        }
        BlogDTO blogDTO = blog.get().toDTO();

        boolean isBlogOwnerNotTheUserAndBlogNotPublished = 
                                    !blogDTO.getOwner().getId().equals(userId) && !blogDTO.isPublised();
        if(isBlogOwnerNotTheUserAndBlogNotPublished) {
            LOGGER.info("Blog {} owner is not the user {} and it is not published!", id, userId);
            return Optional.empty();
        }
        String ownerProfileId = profileIdService.getProfileIdByEntityId(blogDTO.getOwner().getId()).get();
        blogDTO.getOwner().setProfileId(ownerProfileId);

        blogDTO.setDisplayPostedDate(blogUtil.getDisplayPostedData(blogDTO.getPostedTime()));
        return Optional.of(blogDTO);
    }

    @Override
    @UserIdValidator(positions = 0)
    public Optional<BlogDTO> updateBlog(String userId, BlogDTO blogDTO) throws AppException {
        
        Optional<BlogDTO> existingBlog = getBlog(userId, blogDTO.getId());

        if(existingBlog.isEmpty()) {
            LOGGER.error("Blog {} not exists", blogDTO.getId());
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Blog not exists");
        }

        blogUtil.checkAndFillMissingDataForPatchUpdate(blogDTO, existingBlog.get());
        blogUtil.validateAndFormatBlogData(blogDTO);

        Blog savedBlog = blogRepository.save(Blog.build(blogDTO));

        if(savedBlog == null) {
            LOGGER.error("Error while saving blog {}", blogDTO);
            throw new AppException("Errow while saving blog!");
        }
        return Optional.of(savedBlog.toDTO());
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void deleteBlog(String userId, Long id) throws AppException {
        blogRepository.deleteByOwnerIdAndId(userId, id);
    }

    @Override
    @UserIdValidator(positions = 0)
    public List<BlogDTO> getAllBlogsOfUser(String userId) throws AppException {
        List<Blog> blogs = blogRepository.findByOwnerId(userId);

        if(blogs.isEmpty()) {
            return List.of();
        }
        List<BlogDTO> blogDTOs = new ArrayList<>();
        for(Blog blog : blogs) {
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
    public void publishBlog(String userId, Long blogId, String publishAt) throws AppException {
        UserDTO user = userService.getUser(userId).get();
        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(publishAt).orElseThrow(
                                () -> new AppException(HttpStatus.SC_BAD_REQUEST, "Profile Id not exists"));
        BlogDTO blogDTO = getBlog(userId, blogId).get();
        OrganizationDTO organizationDTO = null;

        if(profileIdDTO.getType() == ProfileType.USER) {
            if(!user.getId().equals(profileIdDTO.getEntityId())) {
                LOGGER.error("User {} tried to publish as {}", userId, profileIdDTO.getEntityId());
                throw new AppException(HttpStatus.SC_FORBIDDEN, "You can't publish as other user!");
            }
        }
        else {
            // Validating does user can access organization.
            organizationDTO = organizationService.getOrganization(userId, Long.parseLong(profileIdDTO.getEntityId())).get();
        }

        blogDTO.setPublised(true);
        blogDTO.setPublishedAt(profileIdDTO);
        updateBlog(userId, blogDTO);

        LOGGER.info("Published blog at {}", profileIdDTO.getProfileId());

        List<FollowDTO> followers = followService.getFollowersOfProfile(userId, profileIdDTO.getProfileId());
        NotificationDTO notification = new NotificationDTO();
        notification.setSenderType(NotificationSenderType.USER);
        notification.setMessage(user.getName() + " has published a blog");
        
        if(profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            notification.setSenderType(NotificationSenderType.ORGANIZATION);
            notification.setOrganizationId(organizationDTO.getId());
            notification.setMessage(organizationDTO.getName() + " has published a blog");
        }
        for(FollowDTO follower : followers) {
            notification.setUserId(follower.getFollower().getEntityId());
            notificationService.addNotification(userId, notification);
        }
        LOGGER.info("Notified the profile {} followers", profileIdDTO.getProfileId());
    }
    
    @Override
    @UserIdValidator(positions = 0)
    public Optional<BlogDTO> getBlogOfProfile(String userId, Long blogId, String profileId) throws AppException {

        ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).orElseThrow(
                                () -> new AppException(HttpStatus.SC_BAD_REQUEST, "Profile Id not exists"));

        if(profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Validating does user can access organization.
            organizationService.getOrganization(userId, Long.parseLong(profileIdDTO.getEntityId())).get();
        }
        Optional<Blog> blog = blogRepository.findByIdAndPublishedAtProfileId(blogId, profileId);
        if(blog.isEmpty()) {
            // If the user wants to see his own blog with his profileId
            // But Not getting userId from profileDTO because it will lead to anyone can
            // access the blog with a user's profileId.
            blog = blogRepository.findByOwnerIdAndId(userId, blogId);
            if(blog.isEmpty()) {
                return Optional.empty();
            }
        }
        BlogDTO blogDTO = blog.get().toDTO();
        blogDTO.setDisplayPostedDate(blogUtil.getDisplayPostedData(blogDTO.getPostedTime()));
        return Optional.of(blogDTO);
    }

	@Override
    @UserIdValidator(positions = 0)
	public List<BlogDTO> getAllBlogsOfProfile(String userId, String profileId) throws AppException {
		ProfileIdDTO profileIdDTO = profileIdService.getProfileId(profileId).orElseThrow(
                                () -> new AppException(HttpStatus.SC_BAD_REQUEST, "Profile Id not exists"));

        if(profileIdDTO.getType() == ProfileType.ORGANIZATION) {
            // Validating does user can access organization.
            organizationService.getOrganization(userId, Long.parseLong(profileIdDTO.getEntityId())).get();
        }
        List<Blog> blogs = blogRepository.findByPublishedAtProfileId(profileId);
        return blogs.stream().map(blog -> blog.toDTO()).collect(Collectors.toList());
	}

	@Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
	public void unPublishBlog(String userId, Long blogId) throws AppException {
        BlogDTO blogDTO = getAllBlogsOfUser(userId)
                                .stream()
                                .filter(blog -> blog.getId().equals(blogId))
                                .findFirst()
                                .get();
        
        blogDTO.setPublised(false);
        blogDTO.setPublishedAt(null);
        updateBlog(userId, blogDTO);
        LOGGER.info("UnPublished blog {}", blogId);
	}
}
