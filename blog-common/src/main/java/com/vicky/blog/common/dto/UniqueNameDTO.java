package com.vicky.blog.common.dto;

import lombok.Data;

@Data
public class UniqueNameDTO {
    private Long id;
    private String uniqueName;
    private Long entityId;
}
