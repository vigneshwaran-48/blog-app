package com.vicky.blog.common.dto.search;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {

  public enum SearchType {
    USER,
    ORGANIZATION,
    BLOG,
  }

  public enum SearchBy {
    USER_NAME,
    USER_EMAIL,
    BLOG_TITLE,
    BLOG_CONTENT,
    ORGANIZATION_NAME,
    PROFILE_ID,
    ALL
  }

  @Data
  @AllArgsConstructor
  public class Entity {
    private String id;
    private String profileId;
    private SearchType type;
    private String image;
    private String name;
  }

  private List<Entity> entities;
}
