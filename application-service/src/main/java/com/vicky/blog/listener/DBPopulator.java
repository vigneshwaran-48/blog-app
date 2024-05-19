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
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.model.Tag;

@Component
public class DBPopulator {

    @Autowired
    private TagService tagService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DBPopulator.class);
 
    @EventListener(ContextRefreshedEvent.class)
    public void onEvent() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/tags.json");
            List<Tag> tags = Arrays.asList(mapper.readValue(inputStream, Tag[].class));

            for (Tag tag : tags) {
                if (tagService.getTagByName(tag.getName()).isEmpty()) {
                    tagService.addTag(tag.getName(), tag.getDescription());
                }
            }
            LOGGER.info("Tag created successfully!");
        } catch (IOException | AppException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
