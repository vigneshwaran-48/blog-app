package com.vicky.blog.model;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.organization.OrganizationDTO.JoinType;
import com.vicky.blog.common.dto.organization.OrganizationDTO.Visibility;
import com.vicky.blog.service.organization.OrganizationConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Organization {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = OrganizationConstants.NAME_MAX_LENGTH)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(length = OrganizationConstants.DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private Visibility visibility = Visibility.PUBLIC;

    @Column(name = "join_type", nullable = false)
    private JoinType joinType = JoinType.ANYONE;

    @Column(nullable = false)
    private String image = "http://localhost:8083/static/resource/179rjncje98498983";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        organization.setImage(organizationDTO.getImage());
        organization.setName(organizationDTO.getName());
        organization.setOwner(User.build(organizationDTO.getOwner()));

        if(organizationDTO.getJoinType() != null) {
            organization.setJoinType(organizationDTO.getJoinType());
        }

        if(organizationDTO.getVisibility() != null) {
            organization.setVisibility(organizationDTO.getVisibility());
        }

        return organization;
    }
}
