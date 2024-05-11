package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vicky.blog.common.dto.tag.TagDTO;

import lombok.Data;

@Data
@Document
public class Tag {
    
    @Id
    private String id;
    private String name;
    private String description;

    public TagDTO toDTO() {
        return new TagDTO(id, name, description);
    }
}
