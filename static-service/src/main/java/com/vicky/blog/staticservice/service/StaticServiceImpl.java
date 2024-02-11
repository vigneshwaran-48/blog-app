package com.vicky.blog.staticservice.service;

import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.staticservice.StaticResourceDTO;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.Visibility;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.StaticService;
import com.vicky.blog.staticservice.model.StaticResource;
import com.vicky.blog.staticservice.repository.StaticResourceRepository;

@Service
public class StaticServiceImpl implements StaticService {

    @Autowired
    private StaticResourceRepository staticResourceRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticServiceImpl.class);

    @Override
    public Long addResource(String userId, StaticResourceDTO resource) throws AppException {

        StaticResource staticResource = StaticResource.build(resource);
        staticResource.setUserId(userId);

        StaticResource savedStaticResource = staticResourceRepository.save(staticResource);

        LOGGER.info("Added resource {}", savedStaticResource.getId());
        return savedStaticResource.getId();
    }

    @Override
    public Optional<StaticResourceDTO> getResource(String userId, Long resourceId) throws AppException {
        Optional<StaticResource> resource = staticResourceRepository.findById(resourceId);
        if(resource.isEmpty()) {
            return Optional.empty();
        }
        if(resource.get().getVisibility() == Visibility.PRIVATE && !resource.get().getUserId().equals(userId)) {
            LOGGER.error("The requested resource {} by user {} is accessible", resourceId, userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "Request resource is private");
        }
        return Optional.of(resource.get().toDTO());
    }

    @Override
    public void deleteResource(String userId, Long resourceId) throws AppException {
        Optional<StaticResource> resource = staticResourceRepository.findById(resourceId);
        if(resource.isEmpty()) {
            return;
        }
        if(resource.get().getVisibility() == Visibility.PRIVATE && !resource.get().getUserId().equals(userId)) {
            LOGGER.error("The requested resource {} by user {} is accessible", resourceId, userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "Request resource is private");
        }
        staticResourceRepository.deleteById(resourceId);
    }
    
}
