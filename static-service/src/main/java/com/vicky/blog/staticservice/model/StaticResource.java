package com.vicky.blog.staticservice.model;

import com.vicky.blog.common.dto.staticservice.StaticResourceDTO;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.ContentType;
import com.vicky.blog.common.dto.staticservice.StaticResourceDTO.Visibility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class StaticResource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private ContentType type = ContentType.TEXT_PLAIN;

    @Lob
    @Column(nullable = false, length = 100000)
    private byte[] data;

    @Column(nullable = false)
    private Visibility visibility = Visibility.PUBLIC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
