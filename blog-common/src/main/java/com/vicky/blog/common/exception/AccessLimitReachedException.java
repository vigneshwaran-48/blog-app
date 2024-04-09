package com.vicky.blog.common.exception;

import org.springframework.http.HttpStatus;

import com.vicky.blog.common.dto.blog.BlogFeedsDTO.PageStatus;

public class AccessLimitReachedException extends AppException {

    private final PageStatus pageStatus;

    public AccessLimitReachedException(String message, PageStatus pageStatus) {
        super(HttpStatus.TOO_MANY_REQUESTS.value(), message);
        this.pageStatus = pageStatus;
    }

    public AccessLimitReachedException(PageStatus pageStatus) {
        super(HttpStatus.TOO_MANY_REQUESTS.value(), "Your limit reached for accessing this resource!");
        this.pageStatus = pageStatus;
    }

    public PageStatus getPageStatus() {
        return pageStatus;
    }
}
