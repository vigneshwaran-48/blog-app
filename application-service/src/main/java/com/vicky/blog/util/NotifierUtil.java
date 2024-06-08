package com.vicky.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vicky.blog.util.Notifier.NotifierType;

@Component
public class NotifierUtil {
    
    @Autowired
    private Notifier notifier;

    public void notify(NotifierType type, Object... args) {
        String accessToken = UserContextHolder.getContext().getAccessToken();
        new Thread(() -> {
            notifier.setData(args);
            notifier.execute(accessToken, type);
        }).start();
    }
}
