package com.vicky.blog.service.blog;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.model.Blog;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;

@Component
class BlogUtil {

    @Autowired
    private I18NMessages i18nMessages;

    void validateBlogData(BlogDTO blogDTO) throws AppException {
        validateTitle(blogDTO.getTitle());
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

        // Owner of a blog post can't be changed 
        newBlog.setOwnerId(existingBlog.getOwnerId());
    }

    private void validateTitle(String title) throws AppException {
        if(title != null) {
            if(title.length() < BlogConstants.TITLE_MIN_LENGTH
                || title.length() > BlogConstants.TITLE_MAX_LENGTH) {

                Object[] args = { "Blog title",
                    BlogConstants.TITLE_MIN_LENGTH, BlogConstants.TITLE_MAX_LENGTH };
                throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.MIN_MAX, args));
            }
        }
        else {
            Object[] args = { "Blog title" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, args));
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
}
