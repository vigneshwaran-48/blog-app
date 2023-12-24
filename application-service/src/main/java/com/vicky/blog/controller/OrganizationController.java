package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationResponseData;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/app/organization")
public class OrganizationController {
    
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserIdExtracter userIdExtracter;

    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO, 
            HttpServletRequest request, Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        Optional<OrganizationDTO> organization = organizationService.addOrganization(userId, organizationDTO);
        if(organization.isEmpty()) {
            throw new AppException("Error while adding organization user");
        }

        OrganizationResponseData response = new OrganizationResponseData();
        response.setMessage("Created organization");
        response.setStatus(HttpStatus.SC_CREATED);
        response.setTime(LocalDateTime.now());
        response.setPath(request.getServletPath());
        response.setOrganization(organization.get());

        return ResponseEntity.status(HttpStatus.SC_CREATED).body(response);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<?> getOrganization(@PathVariable Long organizationId, HttpServletRequest request, 
            Principal principal) throws AppException {
        
        String userId = userIdExtracter.getUserId(principal);
        
        OrganizationDTO organization = organizationService.getOrganization(userId, organizationId).orElse(null);
        
        OrganizationResponseData response = new OrganizationResponseData();
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setOrganization(organization);

        return ResponseEntity.ok().body(response);
    }
}
