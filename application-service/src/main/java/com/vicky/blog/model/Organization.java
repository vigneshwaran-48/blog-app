package com.vicky.blog.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationDTO.JoinType;
import com.vicky.blog.common.dto.organization.OrganizationDTO.Visibility;

@Document
public class Organization {
    
    @Id
    private String id;

    private String name;

    @DocumentReference
    private User owner;

    private String description;

    private LocalDateTime createdTime;

    private Visibility visibility = Visibility.PUBLIC;

    private JoinType joinType = JoinType.ANYONE;

    private String image = "http://localhost:7000/static/1";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public OrganizationDTO toDTO(String profileId) {

        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setCreatedTime(createdTime);
        organizationDTO.setDescription(description);
        organizationDTO.setId(id);
        organizationDTO.setImage(image);
        organizationDTO.setJoinType(joinType);
        organizationDTO.setName(name);
        organizationDTO.setOwner(owner.toDTO());
        organizationDTO.setVisibility(visibility);
        organizationDTO.setProfileId(profileId);

        return organizationDTO;
    }

    public static Organization build(OrganizationDTO organizationDTO) {

        Organization organization = new Organization();
        organization.setCreatedTime(organizationDTO.getCreatedTime());
        organization.setDescription(organizationDTO.getDescription());
        organization.setId(organizationDTO.getId());
        organization.setName(organizationDTO.getName());
        organization.setOwner(User.build(organizationDTO.getOwner()));

        if(organizationDTO.getImage() != null) {
            organization.setImage(organizationDTO.getImage());
        }
        if(organizationDTO.getJoinType() != null) {
            organization.setJoinType(organizationDTO.getJoinType());
        }
        if(organizationDTO.getVisibility() != null) {
            organization.setVisibility(organizationDTO.getVisibility());
        }

        return organization;
    }
}
