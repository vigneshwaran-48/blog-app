package com.vicky.blog.staticservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vicky.blog.common.dto.staticservice.StaticResourceDTO;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.ContentType;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.Visibility;

@Document
public class StaticResource {
    
    @Id
    private String id;

    private String userId;

    private String name;

    private ContentType type = ContentType.TEXT_PLAIN;

    private byte[] data;

    private Visibility visibility = Visibility.PUBLIC;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public StaticResourceDTO toDTO() {
        StaticResourceDTO staticResourceDTO = new StaticResourceDTO();
        staticResourceDTO.setId(id);
        staticResourceDTO.setName(name);
        staticResourceDTO.setContentType(type);
        staticResourceDTO.setData(data);
        return staticResourceDTO;
    }

    public static StaticResource build(StaticResourceDTO staticResourceDTO) {

        StaticResource staticResource = new StaticResource();
        staticResource.setId(staticResourceDTO.getId());
        staticResource.setName(staticResourceDTO.getName());
        staticResource.setData(staticResourceDTO.getData());

        if(staticResourceDTO.getContentType() != null) {
            staticResource.setType(staticResourceDTO.getContentType());
        }

        if(staticResourceDTO.getVisibility() != null) {
            staticResource.setVisibility(staticResourceDTO.getVisibility());
        }
        
        return staticResource;
    }
}
