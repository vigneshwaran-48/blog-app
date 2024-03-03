package com.vicky.blog.model;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.service.blog.BlogConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = BlogConstants.TITLE_MAX_LENGTH)
    private String title;

    @Column(nullable = false)
    private String image = "";

    @Column(length = 100000, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(length = BlogConstants.BLOG_DESCRIPTION_LENGTH + 10)
    private String description;

    @Column(name = "posted_time")
    private LocalDateTime postedTime;

    @Column(name = "is_published")
    private boolean isPublised;

    @ManyToOne
    @JoinColumn(name = "published_at")
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
        if(blogDTO.isPublised()) {
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
