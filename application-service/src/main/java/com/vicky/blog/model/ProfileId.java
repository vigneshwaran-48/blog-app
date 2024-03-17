package com.vicky.blog.model;

import com.google.cloud.firestore.annotation.PropertyName;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class ProfileId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "profile_id", unique = true)
    private String profileId;

    @PropertyName("entity_id")
    @Column(name = "entity_id", unique = true)
    private String entityId;

    @Column(nullable = false)
    private ProfileType type;

    @Transient
    private ProfileType profileType;

    @PropertyName("profile_type")
    public String getProfileType() {
        return type.name();
    }

    @PropertyName("profile_type")
    public void setProfileType(String typeName) {
        profileType = ProfileType.valueOf(typeName);
        type = ProfileType.valueOf(typeName);
    }

    public ProfileIdDTO toDTO() {
        ProfileIdDTO profileIdDTO = new ProfileIdDTO();
        profileIdDTO.setEntityId(entityId);
        profileIdDTO.setProfileId(profileId);
        profileIdDTO.setId(id);
        profileIdDTO.setType(type);
        return profileIdDTO;
    }

    public static ProfileId build(ProfileIdDTO profileIdDTO) {

        ProfileId profileId = new ProfileId();
        profileId.setId(profileIdDTO.getId());
        profileId.setEntityId(profileIdDTO.getEntityId());
        profileId.setProfileId(profileIdDTO.getProfileId());
        profileId.setType(profileIdDTO.getType());

        return profileId;
    }
}
