package com.vicky.blog.common.service;

import java.util.Optional;

import com.vicky.blog.common.dto.staticservice.StaticResourceDTO;
import com.vicky.blog.common.exception.AppException;

public interface StaticService {
    
    Long addResource(String userId, StaticResourceDTO resource) throws AppException;

    Optional<StaticResourceDTO> getResource(String userId, Long resourceId) throws AppException;

    void deleteResource(String userId, Long resourceId) throws AppException;
}
