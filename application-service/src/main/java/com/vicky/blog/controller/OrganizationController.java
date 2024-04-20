package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationPermissionResponse;
import com.vicky.blog.common.dto.organization.OrganizationResponseData;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO;
import com.vicky.blog.common.dto.organization.OrganizationUserResponseData;
import com.vicky.blog.common.dto.organization.OrganizationsResponseData;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
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
    private FollowService followService;

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

    @GetMapping
    public ResponseEntity<?> getOrganizationsOfUser(
                HttpServletRequest request, 
                @RequestParam(required = false) UserOrganizationRole role, 
                Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        List<OrganizationDTO> organizations;
        
        if(role != null) {
            organizations = organizationService.getOrganizationUserHasPermission(userId, role);
        }
        else {
            organizations = organizationService.getOrganizationsOfUser(userId);
        }

        OrganizationsResponseData response = new OrganizationsResponseData();
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());
        response.setPath(request.getServletPath());
        response.setOrganizations(organizations);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<?> getOrganization(@PathVariable String organizationId, HttpServletRequest request, 
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

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<?> deleteOrganization(@PathVariable String organizationId, HttpServletRequest request, 
            Principal principal) throws AppException {
        
        String userId = userIdExtracter.getUserId(principal);

        boolean isDeleted = organizationService.deleteOrganization(userId, organizationId);

        if(!isDeleted) {
            throw new AppException("Error while deleting organization");
        }
        
        EmptyResponse response = new EmptyResponse();
        response.setMessage("Deleted organization");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateOrganization(@RequestBody OrganizationDTO organizationDTO, HttpServletRequest request, 
        Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

        Optional<OrganizationDTO> updatedOrganization = organizationService.updateOrganization(userId, organizationDTO);

        OrganizationResponseData response = new OrganizationResponseData();
        response.setMessage("Updated organization!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setOrganization(updatedOrganization.get());

        return ResponseEntity.status(HttpStatus.SC_OK).body(response);
    }

    @PostMapping("/{organizationId}/user")
    public ResponseEntity<?> addUserToOrganization(@PathVariable String organizationId, 
                        @RequestParam("usersToAdd") List<String> usersToAdd,
                        HttpServletRequest request, Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        Optional<OrganizationUserDTO> orgUser = organizationService.addUsersToOrganization(userId, organizationId, usersToAdd);

        OrganizationUserResponseData response = new OrganizationUserResponseData();
        response.setMessage("Added users to organization!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());            
        response.setOrganizationUsers(orgUser.get());
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{organizationId}/user")
    public ResponseEntity<?> getUsersOfOrganization(@PathVariable String organizationId, HttpServletRequest request, 
        Principal principal) throws AppException {
        
        String userId = userIdExtracter.getUserId(principal);
        Optional<OrganizationUserDTO> orgUsers = organizationService.getUsersOfOrganization(userId, organizationId);

        OrganizationUserResponseData response = new OrganizationUserResponseData();
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());            
        response.setOrganizationUsers(orgUsers.get());
        
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{organizationId}/user")
    public ResponseEntity<?> removeUsersFromOrganization(@PathVariable String organizationId,
                        @RequestParam List<String> usersToRemove,
                        HttpServletRequest request, Principal principal) throws AppException {
                        
        String userId = userIdExtracter.getUserId(principal);
        organizationService.removeUsersFromOrganization(userId, organizationId, usersToRemove);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Removed users from organization");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{organizationId}/user/{userToChange}")
    public ResponseEntity<?> changePermissionOfUser(@PathVariable String organizationId, @PathVariable String userToChange, 
            @RequestParam UserOrganizationRole role, HttpServletRequest request, Principal principal) throws AppException {
        
        String userId = userIdExtracter.getUserId(principal);
        organizationService.changePermissionForUser(userId, organizationId, userToChange, role);

        EmptyResponse response = new EmptyResponse();
        response.setMessage("Changed permission of the user");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{organizationId}/notification/access")
    public ResponseEntity<?> isUserHasNotificationAccess(@PathVariable String organizationId, HttpServletRequest request, 
        Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        boolean permission = organizationService.isUserHasAccessToNotification(userId, organizationId);

        OrganizationPermissionResponse response = new OrganizationPermissionResponse();
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setHasPermission(permission);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/following")
    public ResponseEntity<OrganizationsResponseData> getFollowingOrganizations(Principal principal, HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);

        List<OrganizationDTO> followingOrganizations = followService.getFollowingOrganizations(userId);

        OrganizationsResponseData response = new OrganizationsResponseData();
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());
        response.setPath(request.getServletPath());
        response.setOrganizations(followingOrganizations);

        return ResponseEntity.ok().body(response);
    }
}
