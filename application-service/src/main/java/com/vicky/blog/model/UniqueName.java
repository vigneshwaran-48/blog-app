package com.vicky.blog.model;

import com.vicky.blog.common.dto.UniqueNameDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UniqueName {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "unique_name", unique = true)
    private String uniqueName;

    @Column(name = "entity_id", unique = true)
    private Long entityId;

    public UniqueNameDTO toDTO() {
        UniqueNameDTO uniqueNameDTO = new UniqueNameDTO();
        uniqueNameDTO.setEntityId(entityId);
        uniqueNameDTO.setUniqueName(uniqueName);
        uniqueNameDTO.setId(id);
        return uniqueNameDTO;
    }
}
