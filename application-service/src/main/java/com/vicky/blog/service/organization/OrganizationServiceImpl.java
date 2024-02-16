package com.vicky.blog.service.organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationDTO.Visibility;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.OrgUser;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.common.dto.organization.OrganizationUserDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.UniqueNameService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Organization;
import com.vicky.blog.model.OrganizationUser;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.OrganizationRepository;
import com.vicky.blog.repository.OrganizationUserRepository;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationUserRepository organizationUserRepository;

    @Autowired
    private OrganizationUtil organizationUtil;

    @Autowired
    private UniqueNameService uniqueNameService;

    @Override
    public Optional<OrganizationDTO> addOrganization(String userId, OrganizationDTO organizationDTO) throws AppException {
        
        UserDTO user = getUser(userId);
        organizationDTO.setOwner(user);
        organizationDTO.setId(null);

        organizationUtil.validateOrganizationData(organizationDTO, false);
        
        Organization organization = Organization.build(organizationDTO);
        organization.setCreatedTime(LocalDateTime.now());

        Organization addedOrganization = organizationRepository.save(organization);
        if(addedOrganization != null) {
            uniqueNameService.addUniqueName(String.valueOf(addedOrganization.getId()), organizationDTO.getUniqueName());
            addUserToOrg(addedOrganization, User.build(user), UserOrganizationRole.ADMIN);
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

        organizationUtil.validateOrganizationData(organizationDTO, true);

        if(!existingOrganization.get().getOwner().getId().equals(userId)) {
            LOGGER.error("Illegal operation on Organization {} by user {}", organizationDTO.getId(), userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "Only Organization owner can update the organization!");
        }
        
        organizationUtil.checkAndFillMissingDataForPatchUpdate(organizationDTO, existingOrganization.get());

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

        if(organization.get().getVisibility() == Visibility.PRIVATE && !organization.get().getOwner().getId().equals(userId)) {

            List<OrganizationUser> orgUsers = organizationUserRepository.findByOrganizationId(id);
            boolean isPresent = orgUsers.stream().anyMatch(orgUser -> orgUser.getUser().getId().equals(userId));

            if(!isPresent) {
                LOGGER.warn("Illegal access on organization {} by user {}", id, userId);
                throw new AppException(HttpStatus.SC_FORBIDDEN, "You are not allowed to see this organization");
            }
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

        organizationUserRepository.deleteByOrganizationId(id);
        LOGGER.info("Deleted all users from the Organization {}", id);

        organizationRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<OrganizationUserDTO> addUserToOrganization(String userId, Long organizationId, String userToAdd)
            throws AppException {
        
        getUser(userId); // Validating user
        UserDTO userToAddDto = getUser(userToAdd);
        Optional<OrganizationDTO> organization = getOrganization(userId, organizationId);
        
        Optional<OrganizationUser> orgUser = organizationUserRepository.findByOrganizationIdAndUserId(organizationId, userId);
        if(orgUser.isEmpty()) {
            LOGGER.error("User {} is not part of the organization {}", userId, organizationId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "You are not part of the organization!");
        }

        Optional<OrganizationUser> user = organizationUserRepository.findByOrganizationIdAndUserId(organizationId, userToAdd);
        if(user.isPresent()) {
            return Optional.of(getOrganizationUserDTO(user.get()));
        }
      
        OrganizationUser addedUser = addUserToOrg(Organization.build(organization.get()), User.build(userToAddDto), 
                                                UserOrganizationRole.MEMBER);
 
        if(addedUser == null) {
            LOGGER.error("Error while adding user {} to organization {}", userToAdd, organizationId);
            throw new AppException("Errow while adding user");
        }
        LOGGER.info("Added user {} to organization {}", userToAdd, organizationId);
        return Optional.of(getOrganizationUserDTO(addedUser));
    }

    @Override
    public Optional<OrganizationUserDTO> addUsersToOrganization(String userId, Long organizationId,
            List<String> usersToAdd) throws AppException {
        OrganizationUserDTO organizationUserDTO = new OrganizationUserDTO();
        organizationUserDTO.setOrganization(getOrganization(userId, organizationId).get());
        organizationUserDTO.setUsers(new ArrayList<>());

        for(String userToAdd : usersToAdd) {
            Optional<OrganizationUserDTO> orgUser = addUserToOrganization(userId, organizationId, userToAdd);
            organizationUserDTO.getUsers().add(orgUser.get().getUsers().get(0));
        }
        return Optional.of(organizationUserDTO);
    }

    @Override
    public Optional<OrganizationUserDTO> getUsersOfOrganization(String userId, Long organizationId)
            throws AppException {
        getUser(userId); // Validating user
        Optional<OrganizationDTO> organization = getOrganization(userId, organizationId);

        OrganizationUserDTO organizationUserDTO = new OrganizationUserDTO();
        organizationUserDTO.setOrganization(organization.get());
        organizationUserDTO.setUsers(new ArrayList<>());

        List<OrganizationUser> orgUsers = organizationUserRepository.findByOrganizationId(organizationId);
        orgUsers.forEach(orgUser -> {
            OrgUser oUser = organizationUserDTO.new OrgUser(orgUser.getUser().toDTO(), orgUser.getRole());
            organizationUserDTO.getUsers().add(oUser);
        });

        return Optional.of(organizationUserDTO);
    }

    @Override
    public void removeUsersFromOrganization(String userId, Long organizationId, List<String> usersToRemove)
            throws AppException {
        
        getUser(userId); // Validating user
        getOrganization(userId, organizationId); // Validating organization

        Optional<OrganizationUser> organizationUser = 
            organizationUserRepository.findByOrganizationIdAndUserId(organizationId, userId);

        if(organizationUser.isEmpty()) {
            LOGGER.error("User {} is not part of the organization {}", userId, organizationId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "You are not an part of this organization!");
        }
        
        if(organizationUser.get().getRole() != UserOrganizationRole.ADMIN && 
            organizationUser.get().getRole() != UserOrganizationRole.MODERATOR) {
            LOGGER.warn("Illegal operation on organization {} by user {}", organizationId, userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "You don't have privilege to do this operation");
        }

        for(String userToRemove : usersToRemove) {
            if(userId.equals(userToRemove)) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, "You can't remove yourself from the organization");
            }
            getUser(userToRemove); // Validating user
            Optional<OrganizationUser> orgUserToRemove =
                    organizationUserRepository.findByOrganizationIdAndUserId(organizationId, userToRemove);
            
            if(orgUserToRemove.isEmpty()) continue;

            if(orgUserToRemove.get().getRole() == UserOrganizationRole.ADMIN) {
                LOGGER.error("User {} tried to remove the Orgnanization {}'s Admin {}", userId, 
                            organizationId, userToRemove);
                throw new AppException(HttpStatus.SC_FORBIDDEN, "You can't remove the Admin of the organization");
            }
            organizationUserRepository.deleteByOrganizationIdAndUserId(organizationId, userToRemove);

            LOGGER.info("Removed User {} from organization {} by User {}", userToRemove, organizationUser, userId);
        }
    }

    @Override
    public void changePermissionForUser(String userId, Long organizationId, String userToChangePermission,
            UserOrganizationRole role) throws AppException {
        
        getUser(userId); // Validating user
        Optional<OrganizationDTO> organization = getOrganization(userId, organizationId);

        if(organization.isEmpty()) {
            LOGGER.error("Organization {} does not exists", organizationId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Organization does not exists");
        }

        Optional<OrganizationUser> orgUser = organizationUserRepository.findByOrganizationIdAndUserId(organizationId, userId);

        if(orgUser.isEmpty()) {
            LOGGER.error("User {} is not part of the organization {}", userId, organizationId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "You are not part of the organization!");
        }

        if(orgUser.get().getRole() != UserOrganizationRole.ADMIN) {
            LOGGER.error("Illegal operation on Organization {} by user {}", organizationId, userId);
            throw new AppException(HttpStatus.SC_FORBIDDEN, "Only Admin can change the permission of users!");
        }

        Optional<OrganizationUser> orgUserToChange = organizationUserRepository
            .findByOrganizationIdAndUserId(organizationId, userToChangePermission);
        
        if(orgUserToChange.isEmpty()) {
            LOGGER.error("User {} is not part of the organization {}", userToChangePermission, organizationId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User is not part of the organization!");
        }
        orgUserToChange.get().setRole(role);
        OrganizationUser updatedUser = organizationUserRepository.save(orgUserToChange.get());

        if(updatedUser.getRole() == UserOrganizationRole.ADMIN) {
            orgUser.get().setRole(UserOrganizationRole.MODERATOR);
            organizationUserRepository.save(orgUser.get());
            LOGGER.info("Admin {} has been changed to Moderator by himself", userId);

            organization.get().setOwner(updatedUser.getUser().toDTO());
            updateOrganization(userId, organization.get());
        }

        if(orgUserToChange.get().getUser().getId().equals(userId)) {
            // The organization admin changing his own role to some other
            // In this case we will make some moderator or member as Admin.
            OrganizationUser nextAdmin = getNextAdmin(userId, organizationId);
            nextAdmin.setRole(UserOrganizationRole.ADMIN);

            organizationUserRepository.save(nextAdmin);
            LOGGER.info("Next Admin of the organization {} is {}", organizationId, nextAdmin.getUser().getId());
        }

        LOGGER.info("Changed permission of user {} to {}", userToChangePermission, role);
    }

    @Override
    public List<OrganizationDTO> getOrganizationsOfUser(String userId) throws AppException {
        getUser(userId); // Validating user

        List<OrganizationUser> orgsOfUser = organizationUserRepository.findByUserId(userId);

        if(orgsOfUser.isEmpty()) {
            return new ArrayList<>();
        }
        return orgsOfUser
                    .stream()
                    .map(org -> org.getOrganization().toDTO()).collect(Collectors.toList());
    }

    @Override
    public List<OrganizationDTO> getOrganizationUserHasPermission(String userId, UserOrganizationRole role) throws AppException {
        List<OrganizationUser> orgsOfUser = organizationUserRepository.findByUserId(userId);

        List<OrganizationDTO> organizations = new ArrayList<>();

        for(OrganizationUser orgUser: orgsOfUser) {
            if(orgUser.getRole() == role) {
                organizations.add(orgUser.getOrganization().toDTO());
            }
        }
        return organizations;
    }

    private OrganizationUser addUserToOrg(Organization organization, User user, UserOrganizationRole role) {

        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setOrganization(organization);
        organizationUser.setRole(role);
        organizationUser.setUser(user);

        return organizationUserRepository.save(organizationUser);
    }
    
    private UserDTO getUser(String userId) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            LOGGER.error("User {} not exists", userId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User not exists");
        }
        return user.get();
    }

    private OrganizationUserDTO getOrganizationUserDTO(OrganizationUser organizationUser) {
        OrganizationUserDTO dto = new OrganizationUserDTO();
        dto.setOrganization(organizationUser.getOrganization().toDTO());

        OrgUser oUser = dto.new OrgUser(organizationUser.getUser().toDTO(), organizationUser.getRole());
        dto.setUsers(List.of(oUser));

        return dto;
    }

    private OrganizationUser getNextAdmin(String currentAdmin, Long organizationId) throws AppException {

        List<OrganizationUser> moderators = organizationUserRepository
                    .findByOrganizationIdAndRole(organizationId, UserOrganizationRole.MODERATOR);

        if(!moderators.isEmpty() && moderators.size() > 1) {
            // Assuming the currentAdmin is a part of the organization. If this method has to be public
            // then a check for whether the currentAdmin is the organization's member only.

            moderators = moderators
                                .stream()
                                .sorted((orgUser1, orgUser2) -> 
                                        orgUser1.getUser().getName().compareTo(orgUser2.getUser().getName()))
                                .collect(Collectors.toList());
            
            return moderators.get(0);
        }

        // Going to find a member for Admin role here
        List<OrganizationUser> members = organizationUserRepository
                .findByOrganizationIdAndRole(organizationId, UserOrganizationRole.MEMBER);

        if(!members.isEmpty() && members.size() > 1) {
            // Assuming the currentAdmin is a part of the organization
            members = members
                                .stream()
                                .sorted((orgUser1, orgUser2) -> 
                                        orgUser1.getUser().getName().compareTo(orgUser2.getUser().getName()))
                                .collect(Collectors.toList());
            
            return members.get(0);
        }

        LOGGER.error("There are no users to make admin other than the current Admin {}", currentAdmin);
        throw new AppException(HttpStatus.SC_BAD_REQUEST, 
                "There are no users to make admin other than the current Admin");
    }
}
