package com.vicky.blog.util;

import com.vicky.blog.service.blog.BlogPublishNotifier;

public abstract class Notifier {

    public enum NotifierType {
        BLOG_PUBLISH(new BlogPublishNotifier());

        private Notifier notifier;

        NotifierType(Notifier notifier) {
            this.notifier = notifier;
        }

        public Notifier getNotifier() {
            return notifier;
        }
    }
        
    protected Object[] data;

    public void setData(Object... data) {
        this.data = data;
    }

    public void execute(String accessToken, NotifierType notifierType) {
        UserContextHolder.getContext().setAccessToken(accessToken);
        for (NotifierType type : NotifierType.values()) {
            if (notifierType == type) {
                type.getNotifier().execute();
            }
        }
    }

    protected abstract void execute();
}