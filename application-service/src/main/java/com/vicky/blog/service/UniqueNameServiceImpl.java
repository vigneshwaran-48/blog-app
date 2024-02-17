package com.vicky.blog.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.UniqueNameDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UniqueNameService;
import com.vicky.blog.model.UniqueName;
import com.vicky.blog.repository.UniqueNameRepository;

@Service
public class UniqueNameServiceImpl implements UniqueNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueNameServiceImpl.class);

    @Autowired
    private UniqueNameRepository uniqueNameRepository;

    @Override
    public Optional<UniqueNameDTO> addUniqueName(String entityId, String uniqueName) throws AppException {
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
    public Optional<String> getUniqueNameByEntity(String entityId) throws AppException {
        Optional<UniqueName> uniqueNameModel = uniqueNameRepository.findByEntityId(entityId);
        if(uniqueNameModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(uniqueNameModel.get().getUniqueName());
    }

    @Override
    public Optional<UniqueNameDTO> getUniqueName(String uniqueName) throws AppException {
        Optional<UniqueName> uniqueNameModel = uniqueNameRepository.findByUniqueName(uniqueName);
        if(uniqueNameModel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(uniqueNameModel.get().toDTO());
    }

    @Override
    public Optional<UniqueNameDTO> updateUniqueName(String entityId, String uniqueName) throws AppException {
        Optional<UniqueName> uniqueNameModel = uniqueNameRepository.findByEntityId(entityId);
        if(uniqueNameModel.isEmpty()) {
            LOGGER.error("Unique Name not exists for the entity {} already", entityId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Unique Name not exists for the entity already");
        }
        Optional<UniqueNameDTO> uniqueNameDTO = getUniqueName(uniqueName);
        if(uniqueNameDTO.isPresent() && !uniqueNameDTO.get().getEntityId().equals(entityId)) {
            LOGGER.error("Unique Name {} already exists", uniqueName);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Unique Name already exists");
        }
        UniqueName uniqueNameToAdd = new UniqueName();
        uniqueNameToAdd.setId(uniqueNameModel.get().getId());
        uniqueNameToAdd.setEntityId(entityId);
        uniqueNameToAdd.setUniqueName(uniqueName);

        UniqueName savedUniqueName = uniqueNameRepository.save(uniqueNameToAdd);
        if(savedUniqueName == null) {
            LOGGER.error("Error while updating unique name {} for entity {}", uniqueName, entityId);
            throw new AppException("Error while updating unique name");
        }
        return Optional.of(savedUniqueName.toDTO());
    }
    
}
