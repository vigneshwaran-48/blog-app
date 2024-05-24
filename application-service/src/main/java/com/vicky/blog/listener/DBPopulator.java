package com.vicky.blog.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicky.blog.common.dto.blog.BlogConstants;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Tag;

@Component
public class DBPopulator {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DBPopulator.class);
 
    @EventListener(ContextRefreshedEvent.class)
    public void onEvent() {
        try {
            populateTags();
            populateGuestUser();
        } catch (IOException | AppException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void populateTags() throws AppException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/tags.json");
        List<Tag> tags = Arrays.asList(mapper.readValue(inputStream, Tag[].class));

        for (Tag tag : tags) {
            if (tagService.getTagByName(tag.getName()).isEmpty()) {
                tagService.addTag(tag.getName(), tag.getDescription());
            }
        }
        LOGGER.info("Tag created successfully!");
    }

    private void populateGuestUser() throws AppException {
        if (userService.getUser(BlogConstants.GUEST_USER_ID).isPresent()) {
            LOGGER.info("Guest user exists!");
            return;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(BlogConstants.GUEST_USER_ID);
        userDTO.setDescription("Application's guest user's representation");
        userDTO.setEmail("guest@blog.com");
        userDTO.setName("Guest");
        String id = userService.addUser(userDTO);
        LOGGER.info("Created guest user with id {}", id);
    }
}
