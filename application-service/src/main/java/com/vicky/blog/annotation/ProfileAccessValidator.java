package com.vicky.blog.annotation;

public @interface ProfileAccessValidator {
    int userIdPosition() default -1;
    int profileIdPosition() default -1;
}
