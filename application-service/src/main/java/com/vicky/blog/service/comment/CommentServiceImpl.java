package com.vicky.blog.service.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.annotation.BlogIdValidator;
import com.vicky.blog.annotation.CommentIdValidator;
import com.vicky.blog.annotation.UserIdValidator;
import com.vicky.blog.common.dto.blog.BlogDTO;
import com.vicky.blog.common.dto.comment.CommentDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.BlogService;
import com.vicky.blog.common.service.CommentService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.Comment;
import com.vicky.blog.model.CommentLike;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.CommentLikeMongoRepository;
import com.vicky.blog.repository.mongo.CommentMongoRepository;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMongoRepository commentRepository;

    @Autowired
    private CommentLikeMongoRepository commentLikeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
    private static final int MAXIMUM_DEPTH_LEVEL = 2;

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public CommentDTO addComment(String userId, String blogId, String parentCommentId, String commentContent)
        throws AppException {
        
        UserDTO user = userService.getUser(userId).get();
        BlogDTO blog = blogService.getBlog(userId, blogId).get();
        
        Optional<CommentDTO> parentComment = getComment(userId, blogId, parentCommentId);
        
        Comment comment = new Comment();
        comment.setBlog(Blog.build(blog));
        comment.setCommentBy(User.build(user));
        comment.setContent(commentContent);
        if(parentComment.isPresent()) {
            int depthLevel = getDepthLevelOfComment(userId, blogId, parentCommentId);
            if(depthLevel > MAXIMUM_DEPTH_LEVEL) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, 
                    "Can't reply to the comment, Depth level reached!");
            }
            comment.setParentComment(Comment.build(parentComment.get()));
        }
        comment.setCommentedTime(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        if(savedComment == null) {
            LOGGER.info("Error while adding comment to blog {} by user {}", blogId, userId);
            throw new AppException( "Error while adding comment to blog");
        }
        return savedComment.toDTO();
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    @CommentIdValidator(userIdPosition = 0, blogIdPosition = 1, commentIdPosition = 2)
    public CommentDTO editComment(String userId, String blogId, String commentId, String commentContent) throws AppException {
        CommentDTO comment = getComment(userId, blogId, commentId).get();
        comment.setContent(commentContent);
        Comment savedComment = commentRepository.save(Comment.build(comment));
        if(savedComment == null) {
            LOGGER.info("Error while adding comment to blog {} by user {}", blogId, userId);
            throw new AppException( "Error while adding comment to blog");
        }
        return savedComment.toDTO();
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public Optional<CommentDTO> getComment(String userId, String blogId, String commentId) throws AppException {
        Optional<Comment> comment = commentRepository.findByIdAndBlogId(commentId, blogId);
        if(comment.isEmpty()) {
            return Optional.empty();
        }
        int likesCount = commentLikeRepository.findByCommentId(commentId).size();
        boolean isLiked = commentLikeRepository.existsByCommentIdAndLikedById(commentId, userId);
        CommentDTO commentDTO = comment.get().toDTO();
        commentDTO.setCommentLikesCount(likesCount);
        commentDTO.setCurrentUserLikedComment(isLiked);
        return Optional.of(commentDTO);
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void deleteComment(String userId, String blogId, String commentId) throws AppException {
        if(!getThreadsOfComment(userId, blogId, commentId).isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Can't delete comment, Comment has threads!");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void deleteCommentWithItsThreads(String userId, String blogId, String commentId) throws AppException {
        List<CommentDTO> threads = getImmediateNextChildThreadsOfComment(userId, blogId, commentId);
        for(CommentDTO thread : threads) {
            deleteCommentWithItsThreads(userId, blogId, thread.getId());
        }
        deleteComment(userId, blogId, commentId);
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public List<CommentDTO> getThreadsOfComment(String userId, String blogId, String commentId) throws AppException {
        List<Comment> comments = commentRepository.findByParentCommentId(commentId);

        List<CommentDTO> commentDTOs = new ArrayList<>();
        for(Comment comment : comments) {
            List<CommentDTO> threadsOfComment = getThreadsOfComment(userId, blogId, comment.getId());
            CommentDTO commentDTO = comment.toDTO();
            int likesCount = commentLikeRepository.findByCommentId(comment.getId()).size();
            boolean isLiked = commentLikeRepository.existsByCommentIdAndLikedById(comment.getId(), userId);
            commentDTO.setCommentLikesCount(likesCount);
            commentDTO.setThreads(threadsOfComment);
            commentDTO.setCurrentUserLikedComment(isLiked);
            commentDTOs.add(commentDTO);
        }
        return commentDTOs;
    }

    /**
     * Retrieves only the next level of threads. If you need all threads recursively use getThreadsOfComment()
     */
    @Override
	public List<CommentDTO> getImmediateNextChildThreadsOfComment(String userId, String blogId, String commentId)
			throws AppException {
        return commentRepository.findByParentCommentId(commentId)
                                .stream()
                                .map(thread -> thread.toDTO())
                                .collect(Collectors.toList());
	}

	@Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
	public List<CommentDTO> getCommentsOfBlog(String userId, String blogId) throws AppException {
		List<Comment> rootComments = commentRepository.findByBlogIdAndParentCommentIdIsNull(blogId);

        if(rootComments.isEmpty()) {
            return List.of();
        }
        List<CommentDTO> comments = new ArrayList<>();
        for(Comment rootComment : rootComments) {
            List<CommentDTO> threads = getThreadsOfComment(userId, blogId, rootComment.getId());
            CommentDTO rootCommentDTO = rootComment.toDTO();
            int likesCount = commentLikeRepository.findByCommentId(rootComment.getId()).size();
            boolean isLiked = commentLikeRepository.existsByCommentIdAndLikedById(rootComment.getId(), userId);
            rootCommentDTO.setCommentLikesCount(likesCount);
            rootCommentDTO.setThreads(threads);
            rootCommentDTO.setCurrentUserLikedComment(isLiked);
            comments.add(rootCommentDTO);
        }
        return comments;
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void likeComment(String userId, String blogId, String commentId) throws AppException {
        CommentDTO commentDTO = getComment(userId, blogId, commentId).get();
        UserDTO user = userService.getUser(userId).get();

        Optional<CommentLike> existingCommentLike = commentLikeRepository.findByCommentIdAndLikedById(commentId, userId);
        if(existingCommentLike.isPresent()) {
            LOGGER.info("User {} already liked the comment {}", userId, commentId);
            return;
        }

        CommentLike commentLike = new CommentLike();
        commentLike.setComment(Comment.build(commentDTO));
        commentLike.setLikedBy(User.build(user));
        CommentLike savedCommentLike = commentLikeRepository.save(commentLike);
        if(savedCommentLike == null) {
            LOGGER.error("Error saving commentLike");
            throw new AppException("Error while liking the comment");
        }
    }

    @Override
    @UserIdValidator(positions = 0)
    @BlogIdValidator(userIdPosition = 0, blogIdPosition = 1)
    public void removeLike(String userId, String blogId, String commentId) throws AppException {
        commentLikeRepository.deleteByCommentIdAndLikedById(commentId, userId);
    }
    
    private int getDepthLevelOfComment(String userId, String blogId, String commentId) throws AppException {
        int depthLevel = 0;
        Optional<CommentDTO> comment = getComment(userId, blogId, commentId);
        while (comment.isPresent()) {
            depthLevel ++;
            CommentDTO parentComment = comment.get().getParentComment();
            if(parentComment == null) {
                break;
            }
            comment = getComment(userId, blogId, parentComment.getId());
        }
        return depthLevel;
    }
}
