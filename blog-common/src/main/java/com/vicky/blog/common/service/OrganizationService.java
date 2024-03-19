package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.common.exception.AppException;

public interface OrganizationService {

    Optional<OrganizationDTO> addOrganization(String userId, OrganizationDTO organizationDTO) throws AppException;

    Optional<OrganizationDTO> updateOrganization(String userId, OrganizationDTO organizationDTO) throws AppException;

    Optional<OrganizationDTO> getOrganization(String userId, String id) throws AppException;

    boolean deleteOrganization(String userId, String id) throws AppException;

    Optional<OrganizationUserDTO> addUserToOrganization(String userId, String organizationId, String userToAdd)
            throws AppException;

    Optional<OrganizationUserDTO> addUsersToOrganization(String userId, String organizationId, List<String> usersToAdd)
            throws AppException;

    Optional<OrganizationUserDTO> getUsersOfOrganization(String userId, String organizationId) throws AppException;

    void removeUsersFromOrganization(String userId, String organizationId, List<String> usersToRemove)
            throws AppException;

    void changePermissionForUser(String userId, String organizationId, String userToChangePermission,
            UserOrganizationRole role) throws AppException;

    List<OrganizationDTO> getOrganizationsOfUser(String userId) throws AppException;

    List<OrganizationDTO> getOrganizationUserHasPermission(String userId, UserOrganizationRole role)
            throws AppException;

    boolean isUserHasAccessToNotification(String userId, String organizationId) throws AppException;
}