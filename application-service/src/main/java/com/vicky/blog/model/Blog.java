package com.vicky.blog.model;

import java.nio.charset.StandardCharsets;

import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.service.blog.BlogConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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

    @Lob
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public static Blog build(BlogDTO blogDTO, UserDTO userDTO) {

        Blog blog = new Blog();
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent().getBytes(StandardCharsets.UTF_16));
        blog.setId(blogDTO.getId());
        blog.setImage(blogDTO.getImage());
        blog.setOwner(User.build(userDTO));

        return blog;
    }

    public BlogDTO toDTO() {

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(id);
        blogDTO.setTitle(title);
        blogDTO.setContent(new String(content, StandardCharsets.UTF_16));
        blogDTO.setImage(image);
        blogDTO.setOwnerId(owner.getId());
        
        return blogDTO;
    }
}
