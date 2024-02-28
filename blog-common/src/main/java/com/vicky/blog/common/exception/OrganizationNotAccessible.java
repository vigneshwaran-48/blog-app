package com.vicky.blog.common.exception;

import org.springframework.http.HttpStatus;

public class OrganizationNotAccessible extends AppException {
    public OrganizationNotAccessible() {
        super(HttpStatus.FORBIDDEN.value(), "Organization is not accessible");
    }
}
