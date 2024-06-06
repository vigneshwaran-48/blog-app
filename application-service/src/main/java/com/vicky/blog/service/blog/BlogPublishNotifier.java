package com.vicky.blog.service.blog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.common.dto.follower.FollowDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.NotificationService;
import com.vicky.blog.common.utility.Notifier;
import com.vicky.blog.util.UserContextHolder;

@Component
public class BlogPublishNotifier extends Notifier {

    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationService notificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPublishNotifier.class);

    @Override
    public void execute(String accessToken) {

        UserContextHolder.getContext().setAccessToken(accessToken);

        String userId = (String) data[0];
        String profileId = (String) data[1];
        ProfileType profileType = (ProfileType) data[2];
        String userName = (String) data[3];
        String organizationId = (String) data[4];
        String organizationName = (String) data[5];

        try {
            List<FollowDTO> followers = followService.getFollowersOfProfile(userId,profileId);
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
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
}
