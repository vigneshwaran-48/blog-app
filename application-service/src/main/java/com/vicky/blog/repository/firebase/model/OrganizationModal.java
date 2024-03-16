package com.vicky.blog.repository.firebase.model;

import com.google.cloud.firestore.annotation.PropertyName;
import com.vicky.blog.common.dto.organization.OrganizationDTO.JoinType;
import com.vicky.blog.common.dto.organization.OrganizationDTO.Visibility;
import com.vicky.blog.model.Organization;
import java.sql.Timestamp;
import java.time.Instant;

import lombok.Data;

@Data
public class OrganizationModal {

  private Long id;

  private String name;

  private String owner_id;

  private String description;

  private long created_time;

  private Visibility visibility = Visibility.PUBLIC;

  private JoinType join_type = JoinType.ANYONE;

  private Visibility visibilityString = Visibility.PUBLIC;
  private JoinType joinTypeString = JoinType.ANYONE;

  private String image = "http://localhost:7000/static/1";

  @PropertyName("visibilityString")
  public String getVisibilityString() {
    return visibility.name();
  }

  @PropertyName("visibilityString")
  public void setVisibilityString(String visiblityString) {
    this.visibilityString = Visibility.valueOf(visiblityString);
    visibility = Visibility.valueOf(visiblityString);
  }

  @PropertyName("joinTypeString")
  public String getJoinTypeString() {
    return join_type.name();
  }

  @PropertyName("joinTypeString")
  public void setJoinTypeString(String joinTypeString) {
    this.joinTypeString = JoinType.valueOf(joinTypeString);
    join_type = JoinType.valueOf(joinTypeString);
  }

  public static OrganizationModal build(
    com.vicky.blog.model.Organization organization
  ) {
    OrganizationModal organizationModal = new OrganizationModal();
    organizationModal.setId(organization.getId());
    organizationModal.setCreated_time(Timestamp.valueOf(organization.getCreatedTime()).getTime());
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
    organization.setCreatedTime(Timestamp.from(Instant.ofEpochMilli(created_time)).toLocalDateTime());
    organization.setDescription(description);
    organization.setId(id);
    organization.setImage(image);
    organization.setJoinType(join_type);
    organization.setName(name);
    organization.setVisibility(visibility);

    return organization;
  }
}
