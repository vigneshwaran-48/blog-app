package com.vicky.blog.common.dto.blog;

import java.util.List;

import com.vicky.blog.common.dto.bloglike.BlogLikeDTO;
import com.vicky.blog.common.dto.comment.CommentDTO;

import lombok.Data;

@Data
public class BlogFeedDTO {

    private BlogDTO blog;
    private List<BlogLikeDTO> likesOfBlog;
    private List<CommentDTO> comments;

}
