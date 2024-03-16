package com.vicky.blog.repository.firebase.model;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.organization.OrganizationDTO.JoinType;
import com.vicky.blog.common.dto.organization.OrganizationDTO.Visibility;
import com.vicky.blog.model.Organization;

import lombok.Data;

@Data
public class OrganizationModal {
    
    private Long id;

    private String name;

    private String owner_id;

    private String description;

    private LocalDateTime created_time;

    private Visibility visibility = Visibility.PUBLIC;

    private JoinType join_type = JoinType.ANYONE;

    private String image = "http://localhost:7000/static/1";

    public static OrganizationModal build(com.vicky.blog.model.Organization organization) {
        OrganizationModal organizationModal = new OrganizationModal();
        organizationModal.setId(organization.getId());
        organizationModal.setCreated_time(organization.getCreatedTime());
        organizationModal.setDescription(organization.getDescription());
        organizationModal.setImage(organization.getImage());
        organizationModal.setJoin_type(organization.getJoinType());
        organizationModal.setName(organization.getName());
        organizationModal.setOwner_id(organization.getOwner().getId());
        organizationModal.setVisibility(organization.getVisibility());
        return organizationModal;
    }

    public Organization toEntity() {
        Organization organization = new Organization();
        organization.setCreatedTime(created_time);
        organization.setDescription(description);
        organization.setId(id);
        organization.setImage(image);
        organization.setJoinType(join_type);
        organization.setName(name);
        organization.setVisibility(visibility);

        return organization;
    }
}
