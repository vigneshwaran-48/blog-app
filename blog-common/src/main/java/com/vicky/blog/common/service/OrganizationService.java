package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO;
import com.vicky.blog.common.exception.AppException;

public interface OrganizationService {

    Optional<OrganizationDTO> addOrganization(String userId, OrganizationDTO organizationDTO) throws AppException;

    Optional<OrganizationDTO> updateOrganization(String userId, OrganizationDTO organizationDTO) throws AppException;
    
    Optional<OrganizationDTO> getOrganization(String userId, Long id) throws AppException;

    boolean deleteOrganization(String userId, Long id) throws AppException;

    Optional<OrganizationUserDTO> addUserToOrganization(String userId, Long organizationId, String userToAdd) 
        throws AppException;

    Optional<OrganizationUserDTO> addUsersToOrganization(String userId, Long organizationId, List<String> usersToAdd) 
        throws AppException;
}