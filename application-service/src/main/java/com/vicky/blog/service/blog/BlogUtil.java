package com.vicky.blog.service.blog;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Component
class BlogUtil {

    @Autowired
    private I18NMessages i18nMessages;

    void validateAndFormatBlogData(BlogDTO blogDTO) throws AppException {
        formatTitle(blogDTO);
        validateImage(blogDTO.getImage());
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
        setNonEditableData(newBlog, existingBlog);
    }

    String getDescriptionForBlog(String content) {
        String htmlTextContent = Jsoup.parse(content).text();
        if(htmlTextContent.length() >= BlogConstants.BLOG_DESCRIPTION_LENGTH) {
            return htmlTextContent.substring(0, BlogConstants.BLOG_DESCRIPTION_LENGTH);
        }
        return htmlTextContent;
    }

    String getDisplayPostedData(LocalDateTime postedDateTime) {

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

    private String getDateSuffix(int date) {
        switch (date % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private void formatTitle(BlogDTO blog) throws AppException {
        if(blog.getTitle() == null || blog.getTitle().isBlank()) {
            blog.setTitle(BlogConstants.BLOG_EMPTY_TITLE);
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

    private void setNonEditableData(BlogDTO newBlog, BlogDTO existingBlog) {
        newBlog.setOwner(existingBlog.getOwner());
        newBlog.setDescription(getDescriptionForBlog(newBlog.getContent()));
        newBlog.setPostedTime(existingBlog.getPostedTime());
    }
}
