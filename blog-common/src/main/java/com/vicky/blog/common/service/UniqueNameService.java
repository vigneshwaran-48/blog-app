package com.vicky.blog.common.service;

import java.util.Optional;

import com.vicky.blog.common.dto.UniqueNameDTO;
import com.vicky.blog.common.exception.AppException;

public interface UniqueNameService {
    
    Optional<UniqueNameDTO> addUniqueName(String entityId, String uniqueName) throws AppException;
    boolean isUniqueNameExists(String uniqueName) throws AppException;
    Optional<String> getUniqueName(String entityId) throws AppException;

}
