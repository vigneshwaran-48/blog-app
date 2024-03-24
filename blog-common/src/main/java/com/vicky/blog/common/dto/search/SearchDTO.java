package com.vicky.blog.common.dto.search;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDTO {

  public enum SearchType {
    USER,
    ORGANIZATION,
    BLOG,
  }

  public enum SearchBy {
    USER_NAME,
    BLOG_TITLE,
    BLOG_CONTENT,
    ORGANIZATION_NAME,
    PROFILE_ID,
    ALL
  }

  private SearchType type;
  private List<String> entitieIds;
}
