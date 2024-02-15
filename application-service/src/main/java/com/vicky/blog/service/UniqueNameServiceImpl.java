package com.vicky.blog.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vicky.blog.common.dto.UniqueNameDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UniqueNameService;
import com.vicky.blog.model.UniqueName;
import com.vicky.blog.repository.UniqueNameRepository;

public class UniqueNameServiceImpl implements UniqueNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueNameServiceImpl.class);

    private UniqueNameRepository uniqueNameRepository;

    @Override
    public Optional<UniqueNameDTO> addUniqueName(Long entityId, String uniqueName) throws AppException {
        Optional<UniqueName> uniqueNameModel = uniqueNameRepository.findByEntityId(entityId);
        if(uniqueNameModel.isPresent()) {
            LOGGER.error("Already unique name {} exists for {}", uniqueNameModel.get().getUniqueName(), entityId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Already unique name exists for " + entityId);
        }
        if(uniqueNameRepository.existsByUniqueName(uniqueName)) {
            LOGGER.error("Already unique name {} exists", uniqueName);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Unique name already exists");
        }
        UniqueName uNameModel = new UniqueName();
        uNameModel.setEntityId(entityId);
        uNameModel.setUniqueName(uniqueName);
        UniqueName savedUniqueName = uniqueNameRepository.save(uNameModel);
        if(savedUniqueName == null) {
            LOGGER.error("Error while saving unique name {}", uniqueName);
            throw new AppException("Error while saving unique name");
        }
        return Optional.of(savedUniqueName.toDTO());
    }

    @Override
    public boolean isUniqueNameExists(String uniqueName) throws AppException {
        return uniqueNameRepository.existsByUniqueName(uniqueName);
    }

    @Override
    public Optional<String> getUniqueName(Long entityId) throws AppException {
        Optional<UniqueName> uniqueNameModel = uniqueNameRepository.findByEntityId(entityId);
        if(uniqueNameModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(uniqueNameModel.get().getUniqueName());
    }
    
}
