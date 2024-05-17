package com.vicky.blog.staticservice.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.staticservice.StaticResourceDTO;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.ContentType;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.Visibility;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.StaticService;

@Component
public class DBPopulatorListener {

    @Autowired
    private StaticService staticService;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final Logger LOGGER = LoggerFactory.getLogger(DBPopulatorListener.class);
    private static final String DEFAULT_IMAGE_LOCATION = "classpath:static/blog-banner.jpg";

    @EventListener(ContextRefreshedEvent.class)
    public void onEvent() {
        try {
            Optional<StaticResourceDTO> resource =
                    staticService.getResource(null, StaticResourceDTO.DEFAULT_COMPOSE_BANNER_IMAGE_ID);
            if (resource.isEmpty()) {
                storeDefaultImage();
            }
            LOGGER.info("Default banner image exists!");
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void storeDefaultImage() throws AppException {
        StaticResourceDTO resourceDTO = new StaticResourceDTO();
        resourceDTO.setVisibility(Visibility.PUBLIC);
        resourceDTO.setContentType(ContentType.IMAGE_JPG);
        resourceDTO.setId(StaticResourceDTO.DEFAULT_COMPOSE_BANNER_IMAGE_ID);

        try {
            Resource bannerResource = resourceLoader.getResource(DEFAULT_IMAGE_LOCATION);
            resourceDTO.setData(Files.readAllBytes(Paths.get(bannerResource.getURI())));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException("Default banner image file not found!");
        }

        staticService.addResource(null, resourceDTO);
        LOGGER.info("Default Banner Image has been stored!");
    }
}
