package com.vicky.blog.repository.firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.Comment;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.BlogRepository;
import com.vicky.blog.repository.CommentRepository;
import com.vicky.blog.repository.UserRepository;
import com.vicky.blog.repository.firebase.model.CommentModal;

@Repository
@Profile("prod")
public class CommentRepositoryImpl implements CommentRepository {

    private static final String COLLECTION_NAME = "comment";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentRepositoryImpl.class);

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Comment> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Comment> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Comment> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public Comment getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Comment getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Comment getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Comment> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Comment> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Comment> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<Comment> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Comment> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public <S extends Comment> S save(S entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        Long id = entity.getId();
        if(id == null) {
            id = FirebaseUtil.getNextOrderedId(COLLECTION_NAME);
            if(id < 0) {
                return null;
            }
            entity.setId(id);
        }
        CommentModal commentModal = CommentModal.build(entity);
        try {
            firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).set(commentModal).get();
            return entity;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> result = firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).get();
        try {
            DocumentSnapshot snapshot = result.get();
            CommentModal commentModal = snapshot.toObject(CommentModal.class);
            if (commentModal == null) {
                return Optional.empty();
            }
            Comment comment = commentModal.toEntity();
            User commentedUser = userRepository.findById(commentModal.getComment_by()).get();
            /**
             * Not adding blog here if needed in future will be adding.
             */
            comment.setCommentBy(commentedUser);
            
            return Optional.of(comment);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(Long id) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> result = firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).get();
                                                
        try {
            result.get().getReference().delete().get();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void delete(Comment entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Comment> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Comment> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Comment> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Comment> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Comment> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Comment> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Comment, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public List<Comment> findByParentCommentId(Long id) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME)
                                                .whereEqualTo("parent_comment_id", id).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<CommentModal> commentModals = snapshot.toObjects(CommentModal.class);
            if (commentModals.isEmpty()) {
                return List.of();
            }
            List<Comment> comments = new ArrayList<>();
            Blog blog = null;
            for(CommentModal commentModal : commentModals) {
                Comment comment = commentModal.toEntity();
                User commentedUser = userRepository.findById(commentModal.getComment_by()).get();
                if(blog == null) {
                    // Because when finding with parent comment Id all the child blogs must be in the same blog.
                    // Therefore, reducing the firebase call for getting blog.
                    blog = blogRepository.findById(commentModal.getBlog_id()).get();
                }
                comment.setBlog(blog);
                comment.setCommentBy(commentedUser);
                /**
                 * Not adding the parent comment because it will cause a recursive firebase call.
                 * So will be adding it in future if needed but that's not a great idea.
                 */
                comments.add(comment);
            }
            return comments;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Comment> findByIdAndBlogId(Long id, Long blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            return Optional.empty();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        Filter blogFilter = Filter.equalTo("blog_id", blogId);
        Filter commentIdFilter = Filter.equalTo("id", id);
        Filter filter = Filter.and(commentIdFilter, blogFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<CommentModal> commentModals = snapshot.toObjects(CommentModal.class);
            if (commentModals.isEmpty()) {
                return Optional.empty();
            }
            Comment comment = commentModals.get(0).toEntity();
            User commentedUser = userRepository.findById(commentModals.get(0).getComment_by()).get();
            comment.setBlog(blog.get());
            comment.setCommentBy(commentedUser);
            
            return Optional.of(comment);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Comment> findByBlogIdAndParentCommentIdIsNull(Long blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        Filter blogFilter = Filter.equalTo("blog_id", blogId);
        Filter commentIdFilter = Filter.equalTo("parent_comment_id", null);
        Filter filter = Filter.and(commentIdFilter, blogFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<CommentModal> commentModals = snapshot.toObjects(CommentModal.class);
            if (commentModals.isEmpty()) {
                return List.of();
            }
            List<Comment> comments = new ArrayList<>();
            for(CommentModal commentModal : commentModals) {
                Comment comment = commentModal.toEntity();
                User commentedUser = userRepository.findById(commentModal.getComment_by()).get();
                comment.setBlog(blog.get());
                comment.setCommentBy(commentedUser);
                /**
                 * Not adding the parent comment because it will cause a recursive firebase call.
                 * So will be adding it in future if needed but that's not a great idea.
                 */
                comments.add(comment);
            }
            return comments;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }
    
}
