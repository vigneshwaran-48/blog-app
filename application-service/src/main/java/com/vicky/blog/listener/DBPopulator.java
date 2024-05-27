package com.vicky.blog.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vicky.blog.common.dto.blog.BlogConstants;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserDTO.Gender;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.common.service.TagService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Tag;

@Component
public class DBPopulator {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private BlogService blogService;

    @Value("${services.api-gateway.base}")
    private String apiGatewayBase;

    private static final Logger LOGGER = LoggerFactory.getLogger(DBPopulator.class);

    @EventListener(ContextRefreshedEvent.class)
    public void onEvent() {
        try {
            populateTags();
            populateGuestUser();
            createBots();
            populateBotBlogs();
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

    private void createBots() throws AppException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/bots.json");
        List<UserDTO> bots = Arrays.asList(mapper.readValue(inputStream, UserDTO[].class));

        for (UserDTO bot : bots) {
            if (userService.getUser(bot.getId()).isPresent()) {
                continue;
            }
            bot.setImage(getImageForBot(bot.getGender()));
            userService.addUser(bot);
        }
        LOGGER.info("Created bots!");
    }

    private String getImageForBot(Gender gender) {
        int max = 6;
        int min = 0;
        int randomNumber = (int) (Math.random() * (max - min)) + min;
        String offsetStr = randomNumber == 0 ? "" : "-" + randomNumber;
        String imageName = "avatar-" + gender.name().toLowerCase() + offsetStr;
        return apiGatewayBase + "/static/" + imageName + ".jpg";
    }

    private void populateBotBlogs() throws AppException, IOException {
        UserDTO userDTO = userService.getUser("bot_15").get();
        ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();
        InputStream inputStream = getClass().getResourceAsStream("/blogs.json");
        List<BlogDTO> blogs = Arrays.asList(mapper.readValue(inputStream, BlogDTO[].class));
        for (BlogDTO blog : blogs) {
            LOGGER.info(blog.toString());
            blog.setOwner(userDTO);
            blog.setPublised(true);
            blog.setPublishedAt(profileIdService.getProfileId(userDTO.getProfileId()).get());
            String blogId = blogService.addBlog(userDTO.getId(), blog);
            LOGGER.info("Added blog {}", blogId);
        }
    }

}
