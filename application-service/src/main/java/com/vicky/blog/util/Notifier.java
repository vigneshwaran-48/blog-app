package com.vicky.blog.util;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.NotificationService;

@Component
public class Notifier {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FollowService followService;

    private static final Logger LOGGER = LoggerFactory.getLogger(Notifier.class);

    public enum NotifierType {
        BLOG_PUBLISH,
        COMMENT_NOTIFIER;
    }

    protected Object[] data;

    public void setData(Object... data) {
        this.data = data;
    }

    public void execute(String accessToken, NotifierType notifierType) {
        UserContextHolder.getContext().setAccessToken(accessToken);
        try {
            switch (notifierType) {
            case BLOG_PUBLISH:
                notifyBlogPublish();
                break;
            case COMMENT_NOTIFIER:
                notifyComment();
                break;

            default:
                break;
            }
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void notifyBlogPublish() throws AppException {
        String userId = (String) data[0];
        String profileId = (String) data[1];
        ProfileType profileType = (ProfileType) data[2];
        String userName = (String) data[3];
        String organizationId = (String) data[4];
        String organizationName = (String) data[5];

        List<FollowDTO> followers = followService.getFollowersOfProfile(userId, profileId);
        NotificationDTO notification = new NotificationDTO();
        notification.setSenderType(NotificationSenderType.USER);
        notification.setMessage(userName + " has published a blog");

        if (profileType == ProfileType.ORGANIZATION) {
            notification.setSenderType(NotificationSenderType.ORGANIZATION);
            notification.setOrganizationId(organizationId);
            notification.setMessage(organizationName + " has published a blog");
        }
        for (FollowDTO follower : followers) {
            notification.setUserId(follower.getFollower().getEntityId());
            notificationService.addNotification(userId, notification);
        }
        LOGGER.info("Notified the profile {} followers", profileId);
    }

    private void notifyComment() throws AppException {
        UserDTO commentedUser = (UserDTO) data[0];
        UserDTO blogOwner = (UserDTO) data[1];
        BlogDTO blogDTO = (BlogDTO) data[2];

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setSenderId(commentedUser.getId());
        notificationDTO.setUserId(blogOwner.getId());
        notificationDTO.setMessage("Hey " + blogOwner.getName() + ",\r\n" + commentedUser.getName()
                + " commented on your blog " + blogDTO.getTitle());
        notificationDTO.setSenderType(NotificationSenderType.USER);
        notificationDTO.setSenderImage(commentedUser.getImage());
        notificationDTO.setTime(LocalDateTime.now());

        notificationService.addNotification(commentedUser.getId(), notificationDTO);
        LOGGER.info("Notified for the comment action!");
    }
}