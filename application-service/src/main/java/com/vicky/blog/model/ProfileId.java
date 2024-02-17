package com.vicky.blog.model;

import com.vicky.blog.common.dto.ProfileIdDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProfileId {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "unique_name", unique = true)
    private String profileId;

    @Column(name = "entity_id", unique = true)
    private String entityId;

    public ProfileIdDTO toDTO() {
        ProfileIdDTO profileIdDTO = new ProfileIdDTO();
        profileIdDTO.setEntityId(entityId);
        profileIdDTO.setProfileId(profileId);
        profileIdDTO.setId(id);
        return profileIdDTO;
    }
}
