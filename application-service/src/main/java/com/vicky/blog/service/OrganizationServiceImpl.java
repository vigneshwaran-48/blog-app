package com.vicky.blog.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationDTO.Visibility;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Organization;
import com.vicky.blog.repository.OrganizationRepository;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Optional<OrganizationDTO> addOrganization(String userId, OrganizationDTO organizationDTO) throws AppException {
        
        UserDTO user = getUser(userId);
        organizationDTO.setOwner(user);
        
        Organization organization = Organization.build(organizationDTO);
        organization.setCreatedTime(LocalDateTime.now());

        Organization addedOrganization = organizationRepository.save(organization);
        if(addedOrganization != null) {
            return Optional.of(addedOrganization.toDTO());
        }
        LOGGER.error("Created organization is null, Requested DTO", organizationDTO);
        return Optional.empty();
    }

    @Override
    public Optional<OrganizationDTO> updateOrganization(String userId, OrganizationDTO organizationDTO)
            throws AppException {
        
        getUser(userId); // Validating user
        Optional<Organization> existingOrganization = organizationRepository.findById(organizationDTO.getId());
        
        if(existingOrganization.isEmpty()) {
            LOGGER.error("Organization {} does not exists", organizationDTO.getId());
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Organization does not exists");
        }

        if(!existingOrganization.get().getOwner().getId().equals(userId)) {
            LOGGER.error("Illegal operation on Organization {} by user {}", organizationDTO.getId(), userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "Only Organization owner can update the organization!");
        }
        // Not allowing to change the created time of the organization
        organizationDTO.setCreatedTime(existingOrganization.get().getCreatedTime());

        if(organizationDTO.getDescription() == null) {
            organizationDTO.setDescription(existingOrganization.get().getDescription());
        }
        if(organizationDTO.getImage() == null) {
            organizationDTO.setImage(existingOrganization.get().getImage());
        }
        if(organizationDTO.getJoinType() == null) {
            organizationDTO.setJoinType(existingOrganization.get().getJoinType());
        }
        if(organizationDTO.getName() == null) {
            organizationDTO.setName(existingOrganization.get().getName());
        }
        if(organizationDTO.getOwner() == null) {
            organizationDTO.setOwner(existingOrganization.get().getOwner().toDTO());
        }
        if(organizationDTO.getVisibility() == null) {
            organizationDTO.setVisibility(existingOrganization.get().getVisibility());
        }
        Organization updatedOrganization = organizationRepository.save(Organization.build(organizationDTO));
        if(updatedOrganization == null) {
            throw new AppException("Error while updating organization!");
        }
        return Optional.of(updatedOrganization.toDTO());
    }

    @Override
    public Optional<OrganizationDTO> getOrganization(String userId, Long id) throws AppException {
        
        getUser(userId); // validating user
        Optional<Organization> organization = organizationRepository.findById(id);

        if(organization.isEmpty()) {
            LOGGER.error("Organization {} not exists", id);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Invalid organization id");
        }

        // TODO Need to include org members also for checking access.
        if(organization.get().getVisibility() == Visibility.PRIVATE && !organization.get().getOwner().getId().equals(userId)) {
            LOGGER.warn("Illegal access on organization {} by user {}", id, userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "You are not allowed to see this organization");
        }

        return Optional.of(organization.get().toDTO());
    }

    @Override
    public boolean deleteOrganization(String userId, Long id) throws AppException {
        Optional<Organization> organization = organizationRepository.findById(id);
        if(organization.isEmpty()) {
            LOGGER.error("Organization {} does not exists", id);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Organization does not exists");
        }

        if(!organization.get().getOwner().getId().equals(userId)) {
            LOGGER.error("Illegal operation on Organization {} by user {}", id, userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "Only organization owner can delete the organization");
        }

        organizationRepository.deleteById(id);
        return true;
    }
    
    private UserDTO getUser(String userId) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            LOGGER.error("User {} not exists", userId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User not exists");
        }
        return user.get();
    }
}
