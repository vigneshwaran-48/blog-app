package com.vicky.blog.notificationservice.service;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationPermissionResponse;
import com.vicky.blog.common.dto.organization.OrganizationResponseData;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.notificationservice.client.OrganizationServiceClient;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationServiceClient organizationServiceClient;

    @Override
    public Optional<OrganizationDTO> addOrganization(String userId, OrganizationDTO organizationDTO)
            throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addOrganization'");
    }

    @Override
    public Optional<OrganizationDTO> updateOrganization(String userId, OrganizationDTO organizationDTO)
            throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOrganization'");
    }

    @Override
    public Optional<OrganizationDTO> getOrganization(String userId, String id) throws AppException {
        
        OrganizationResponseData response = organizationServiceClient.getOrganization(id);
        if(response.getStatus() != HttpStatus.SC_OK) {
            throw new AppException(response.getStatus(), response.getMessage());
        }
        return Optional.of(response.getOrganization());
    }

    @Override
    public boolean deleteOrganization(String userId, String id) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrganization'");
    }

    @Override
    public Optional<OrganizationUserDTO> addUserToOrganization(String userId, String organizationId, String userToAdd)
            throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUserToOrganization'");
    }

    @Override
    public Optional<OrganizationUserDTO> addUsersToOrganization(String userId, String organizationId,
            List<String> usersToAdd) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUsersToOrganization'");
    }

    @Override
    public Optional<OrganizationUserDTO> getUsersOfOrganization(String userId, String organizationId)
            throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsersOfOrganization'");
    }

    @Override
    public void removeUsersFromOrganization(String userId, String organizationId, List<String> usersToRemove)
            throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeUsersFromOrganization'");
    }

    @Override
    public void changePermissionForUser(String userId, String organizationId, String userToChangePermission,
            UserOrganizationRole role) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePermissionForUser'");
    }

    @Override
    public List<OrganizationDTO> getOrganizationsOfUser(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrganizationsOfUser'");
    }

    @Override
    public List<OrganizationDTO> getOrganizationUserHasPermission(String userId, UserOrganizationRole role)
            throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrganizationUserHasPermission'");
    }

    @Override
    public boolean isUserHasAccessToNotification(String userId, String organizationId) throws AppException {
        OrganizationPermissionResponse response = organizationServiceClient.isUserHasNotificationAccess(organizationId);
        if(response.getStatus() != HttpStatus.SC_OK) {
            throw new AppException(response.getStatus(), response.getMessage());
        }
        return response.isHasPermission();
    }

    @Override
    public List<OrganizationDTO> getOrganizationsVisibleToUser(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrganizationsVisibleToUser'");
    }
    
}
