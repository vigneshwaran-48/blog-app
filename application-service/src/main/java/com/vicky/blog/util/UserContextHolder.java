package com.vicky.blog.util;

public class UserContextHolder {

    private UserContextHolder() {
    }

    private static final ThreadLocal<UserContext> contexts = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = contexts.get();
        if(context == null) {
            contexts.set(createEmptyContext());
        }
        return contexts.get();
    }

    public static void clearContext() {
        contexts.remove();
    }

    private static UserContext createEmptyContext() {
        return new UserContext();
    }
}
