package com.vicky.blog.staticservice.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vicky.blog.common.dto.staticservice.StaticResourceDTO;
import com.vicky.blog.common.dto.staticservice.StaticResourceResponse;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.ContentType;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.Visibility;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.StaticService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/static")
public class StaticResourceController {

    @Autowired
    private UserIdExtracter userIdExtracter;

    @Autowired
    private StaticService staticService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticResourceController.class);
    
    @PostMapping
    public ResponseEntity<?> addResource(@RequestParam("resource") MultipartFile resource, 
        @RequestParam(name = "private", required = false) Boolean isPrivate, HttpServletRequest request,
        Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        ContentType contentType = ContentType.getContenType(resource.getContentType());

        if(contentType == null) {
            throw new AppException(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, 
                                    "Unsupported Resource type " + resource.getContentType());
        }

        StaticResourceDTO staticResource = new StaticResourceDTO();

        try {
        
            staticResource.setContentType(contentType);
            staticResource.setName(resource.getName());
            staticResource.setData(resource.getBytes());
            if(isPrivate != null && isPrivate) {
                staticResource.setVisibility(Visibility.PRIVATE);
            }
        }
        catch(Exception e) {
            LOGGER.error("Error while parsing the resource", e);
            throw new AppException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Error while parsing the resource");
        }

        String resourceId = staticService.addResource(userId, staticResource);

        StaticResourceResponse response = new StaticResourceResponse();
        response.setId(resourceId);
        response.setMessage("Added resource");
        response.setStatus(HttpStatus.SC_CREATED);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.SC_CREATED).body(response);
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<?> getResource(@PathVariable("resourceId") String resourceId, HttpServletRequest request, 
        Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        Optional<StaticResourceDTO> resource = staticService.getResource(userId, resourceId);

        if(resource.isEmpty()) {
            throw new AppException(HttpStatus.SC_NOT_FOUND, "The requested resource not found");
        }

        String contentType = resource.get().getContentType().getType();

        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(resource.get().getData());
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<?> deleteResource(@PathVariable("resourceId") String resourceId, HttpServletRequest request, 
        Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        staticService.deleteResource(userId, resourceId);

        StaticResourceResponse response = new StaticResourceResponse();
        response.setId(resourceId);
        response.setMessage("Deleted resource");
        response.setStatus(HttpStatus.SC_CREATED);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
    
}
