package com.vicky.blog.common.dto.tag;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO implements Serializable {
    
    private String id;
    private String name;
    private String description;
}
