package com.vicky.blog.repository.firebase.model;

import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.model.OrganizationUser;

import lombok.Data;

@Data
public class OrganizationUserModal {

    private Long id;
    private Long organization_id;
    private String user_id;
    private UserOrganizationRole role = UserOrganizationRole.MEMBER;

    public static OrganizationUserModal build(OrganizationUser organizationUser) {
        OrganizationUserModal organizationUserModal = new OrganizationUserModal();
        organizationUserModal.setId(organizationUser.getId());
        organizationUserModal.setOrganization_id(organizationUser.getOrganization().getId());
        organizationUserModal.setRole(organizationUser.getRole());
        organizationUserModal.setUser_id(organizationUser.getUser().getId());
        return organizationUserModal;
    }

    public OrganizationUser toEntity() {
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setId(id);
        organizationUser.setRole(role);
        return organizationUser;
    }
}
