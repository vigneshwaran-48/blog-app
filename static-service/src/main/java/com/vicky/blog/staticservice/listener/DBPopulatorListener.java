package com.vicky.blog.staticservice.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final String USER_PROFILE_LOCATION = "classpath:static/user-profile";
    private static final String BLOG_LOCATION = "classpath:static/blog";

    @EventListener(ContextRefreshedEvent.class)
    public void onEvent() {
        try {
            Optional<StaticResourceDTO> resource =
                    staticService.getResource(null, StaticResourceDTO.DEFAULT_COMPOSE_BANNER_IMAGE_ID);
            if (resource.isEmpty()) {
                storeDefaultImage();
            }
            LOGGER.info("Default banner image exists!");

            storeAvatars();
            storeBannerImages();
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

    private void storeAvatars() throws AppException {
        try {
            Resource userProfileDirResource = resourceLoader.getResource(USER_PROFILE_LOCATION);
            Path path = Paths.get(userProfileDirResource.getURI());

            try (Stream<Path> paths = Files.walk(path, 1)) {
                List<Path> files = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                for (Path avatar : files) {
                    if (staticService.getResource(null, avatar.getFileName().toString()).isPresent()) {
                        continue;
                    }
                    StaticResourceDTO staticResourceDTO = new StaticResourceDTO();
                    staticResourceDTO.setVisibility(Visibility.PUBLIC);
                    staticResourceDTO.setContentType(ContentType.IMAGE_JPG);
                    staticResourceDTO.setData(Files.readAllBytes(avatar));
                    staticResourceDTO.setId(avatar.getFileName().toString());
                    staticService.addResource(null, staticResourceDTO);
                }  
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException("Error while storing avatars!");
        }
        LOGGER.info("Stored avatars!");
    }

    private void storeBannerImages() {
        try {
            Resource blogDirResource = resourceLoader.getResource(BLOG_LOCATION);
            Path path = Paths.get(blogDirResource.getURI());

            try (Stream<Path> paths = Files.walk(path, 1)) {
                List<Path> files = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                for (Path blogBanner : files) {
                    if (staticService.getResource(null, blogBanner.getFileName().toString()).isPresent()) {
                        continue;
                    }
                    StaticResourceDTO staticResourceDTO = new StaticResourceDTO();
                    staticResourceDTO.setVisibility(Visibility.PUBLIC);
                    staticResourceDTO.setContentType(ContentType.IMAGE_JPG);
                    staticResourceDTO.setData(Files.readAllBytes(blogBanner));
                    staticResourceDTO.setId(blogBanner.getFileName().toString());
                    staticService.addResource(null, staticResourceDTO);
                }  
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
