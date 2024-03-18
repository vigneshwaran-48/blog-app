package com.vicky.blog.model;

import com.vicky.blog.common.dto.blog.BlogDTO;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document
public class Blog {

  @Id
  private Long id;

  private String title;

  private String image = "";

  private String content;

  @DocumentReference
  private User owner;

  private String description;

  private LocalDateTime postedTime;

  private boolean isPublised;

  @DocumentReference
  private ProfileId publishedAt;

  public static Blog build(BlogDTO blogDTO) {
    Blog blog = new Blog();
    blog.setTitle(blogDTO.getTitle());
    blog.setContent(blogDTO.getContent());
    blog.setId(blogDTO.getId());
    blog.setImage(blogDTO.getImage());
    blog.setOwner(User.build(blogDTO.getOwner()));
    blog.setDescription(blogDTO.getDescription());
    blog.setPostedTime(blogDTO.getPostedTime());
    blog.setPublised(blogDTO.isPublised());
    if (blogDTO.isPublised()) {
      // Not doing null check because if the blog is published then there should be publishedAt value.
      blog.setPublishedAt(ProfileId.build(blogDTO.getPublishedAt()));
    }

    return blog;
  }

  public BlogDTO toDTO(String profileId) {
    BlogDTO blogDTO = new BlogDTO();
    blogDTO.setId(id);
    blogDTO.setTitle(title);
    blogDTO.setContent(content);
    blogDTO.setImage(image);
    blogDTO.setOwner(owner.toDTO());
    blogDTO.setDescription(description);
    blogDTO.setPostedTime(postedTime);
    blogDTO.setPostedProfileId(profileId);
    blogDTO.setPublised(isPublised);
    blogDTO.setPublishedAt(publishedAt.toDTO());

    return blogDTO;
  }

  public BlogDTO toDTO() {
    BlogDTO blogDTO = new BlogDTO();
    blogDTO.setId(id);
    blogDTO.setTitle(title);
    blogDTO.setContent(content);
    blogDTO.setImage(image);
    blogDTO.setOwner(owner.toDTO());
    blogDTO.setDescription(description);
    blogDTO.setPostedTime(postedTime);
    blogDTO.setPublised(isPublised);
    blogDTO.setPublishedAt(publishedAt != null ? publishedAt.toDTO() : null);

    return blogDTO;
  }
}
