package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.cloud.firestore.annotation.PropertyName;
import com.vicky.blog.common.dto.profile.ProfileIdDTO;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;

import lombok.Data;

@Document
@Data
public class ProfileId {

    @Id
    private Long id;

    private String profileId;

    private String entityId;

    private ProfileType type;

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
