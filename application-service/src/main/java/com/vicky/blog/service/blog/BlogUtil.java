package com.vicky.blog.service.blog;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.exception.OrganizationNotAccessible;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.repository.mongo.BlogMongoRepository;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;
import com.vicky.blog.service.organization.OrganizationConstants;

@Component
public class BlogUtil {

    @Autowired
    private I18NMessages i18nMessages;

    @Autowired
    private BlogMongoRepository blogRepository;

    @Autowired
    private OrganizationService organizationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogUtil.class);

    void validateAndFormatBlogData(BlogDTO blogDTO) throws AppException {
        validateTitle(blogDTO.getTitle());
        validateImage(blogDTO.getImage());
        validateBlogPublish(blogDTO.isPublised(), blogDTO.getPublishedAt());
    }

    void checkAndFillMissingDataForPatchUpdate(BlogDTO newBlog, BlogDTO existingBlog) {

        if(newBlog.getContent() == null) {
            newBlog.setContent(existingBlog.getContent());
        }
        if(newBlog.getImage() == null) {
            newBlog.setImage(existingBlog.getImage());
        }
        if(newBlog.getTitle() == null) {
            newBlog.setTitle(existingBlog.getTitle());
        }
        if(!newBlog.isPublised()) {
            newBlog.setPublishedAt(null);
        }
        setNonEditableData(newBlog, existingBlog);
    }

    String getDescriptionForBlog(String content) {
        String htmlTextContent = Jsoup.parse(content).text();
        if(htmlTextContent.length() >= BlogConstants.BLOG_DESCRIPTION_LENGTH) {
            return htmlTextContent.substring(0, BlogConstants.BLOG_DESCRIPTION_LENGTH);
        }
        return htmlTextContent;
    }

    public String getDisplayPostedData(LocalDateTime postedDateTime) {

        long minutesDifference = ChronoUnit.MINUTES.between(postedDateTime, LocalDateTime.now());
        if(minutesDifference <= 60) {
            return minutesDifference + (minutesDifference == 1 ? " minute ago" : " minutes ago");
        }
        long hoursDifference = ChronoUnit.HOURS.between(postedDateTime, LocalDateTime.now());
        if(hoursDifference <= 24) {
            return hoursDifference + (hoursDifference == 1 ? " hour ago" : " hours ago");
        }
        long daysDifference = ChronoUnit.DAYS.between(postedDateTime, LocalDateTime.now());
        if(daysDifference <= 3) {
            return daysDifference + (daysDifference == 1 ? " day ago" : " days ago");
        }
        int date = postedDateTime.getDayOfMonth();
        return "on " + date + getDateSuffix(date) + " " + postedDateTime.getMonth().name();
    }

    List<BlogDTO> getPostedBlogsOfUser(String userId, int page, int size) throws AppException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "postedTime"));
        Page<Blog> blogs = blogRepository.findByOwnerIdAndIsPublised(userId, true, pageable);
        List<BlogDTO> blogDTOs = new ArrayList<>();
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
            blogDTO.setDisplayPostedDate(getDisplayPostedData(blogDTO.getPostedTime()));
            blogDTOs.add(blogDTO);
        }
        return blogDTOs;
    }

    private String getDateSuffix(int date) {
        switch (date % 10) {
            case 1:
                return "st";
            case 2:
                if(date < 20) {
                    return "th";
                }
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private void validateImage(String image) throws AppException {
        Object[] args = { "image" };
        try {
            new URL(image).toURI();
        } catch (MalformedURLException e) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.INVALID, args));
        } catch (URISyntaxException e) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.INVALID, args));
        }
    }

    private void validateTitle(String title) throws AppException {
        if(title == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, 
                i18nMessages.getMessage(I18NMessage.REQUIRED, new Object[] { "Title" }));
        }
        if(title.length() > BlogConstants.TITLE_MAX_LENGTH) {
            Object[] args = { "Blog Title", 0, OrganizationConstants.NAME_MAX_LENGTH };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.MIN_MAX, args));
        }
    }

    private void setNonEditableData(BlogDTO newBlog, BlogDTO existingBlog) {
        newBlog.setOwner(existingBlog.getOwner());
        newBlog.setDescription(getDescriptionForBlog(newBlog.getContent()));
        newBlog.setPostedTime(existingBlog.getPostedTime());
    }

    private void validateBlogPublish(boolean isPublished, ProfileIdDTO publishedAt) throws AppException {
        if(isPublished && publishedAt == null) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, 
                i18nMessages.getMessage(I18NMessage.REQUIRED, new Object[] { "publishAt" }));
        }
    }
}
