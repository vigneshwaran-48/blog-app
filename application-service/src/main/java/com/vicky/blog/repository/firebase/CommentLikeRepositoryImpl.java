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
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.vicky.blog.model.Comment;
import com.vicky.blog.model.CommentLike;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.CommentLikeRepository;
import com.vicky.blog.repository.CommentRepository;
import com.vicky.blog.repository.UserRepository;
import com.vicky.blog.repository.firebase.model.CommentLikeModal;

@Repository
@Profile("prod")
public class CommentLikeRepositoryImpl implements CommentLikeRepository {

    private static final String COLLECTION_NAME = "comment_like";
    private static Logger LOGGER = LoggerFactory.getLogger(CommentLikeRepositoryImpl.class);

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends CommentLike> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends CommentLike> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<CommentLike> entities) {
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
    public CommentLike getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public CommentLike getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public CommentLike getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends CommentLike> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends CommentLike> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends CommentLike> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<CommentLike> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<CommentLike> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public <S extends CommentLike> S save(S entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        long id = FirebaseUtil.getUniqueLong();
        entity.setId(id);
        CommentLikeModal commentLikeModal = CommentLikeModal.build(entity);
        try {
            firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).set(commentLikeModal).get();
            return entity;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<CommentLike> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void delete(CommentLike entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends CommentLike> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<CommentLike> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<CommentLike> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends CommentLike> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends CommentLike> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends CommentLike> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends CommentLike> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends CommentLike, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public List<CommentLike> findByCommentId(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME)
                                                .whereEqualTo("comment_id", commentId).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<CommentLikeModal> commentLikeModals = snapshot.toObjects(CommentLikeModal.class);
            if (commentLikeModals.isEmpty()) {
                return List.of();
            }
            List<CommentLike> commentLikes = new ArrayList<>();
            for(CommentLikeModal commentLikeModal : commentLikeModals) {
                CommentLike commentLike = commentLikeModal.toEntity();
                User user = userRepository.findById(commentLikeModal.getLiked_user_id()).get();
                commentLike.setLikedBy(user);
                commentLike.setComment(comment.get());
                commentLikes.add(commentLike);
            }
            return commentLikes;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<CommentLike> findByCommentIdAndLikedById(Long commentId, String userId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter commentFilter = Filter.equalTo("comment_id", commentId);
        Filter userFilter = Filter.equalTo("liked_user_id", userId);
        Filter filter = Filter.and(commentFilter, userFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<CommentLikeModal> commentLikeModals = snapshot.toObjects(CommentLikeModal.class);
            if (commentLikeModals.isEmpty()) {
                return Optional.empty();
            }
            CommentLike commentLike = commentLikeModals.get(0).toEntity();
            Comment comment = commentRepository.findById(commentId).get();
            User likedByUser = userRepository.findById(userId).get();
            commentLike.setComment(comment);
            commentLike.setLikedBy(likedByUser);
            return Optional.of(commentLike);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByCommentIdAndLikedById(Long commentId, String userId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter commentProfileFilter = Filter.equalTo("comment_id", commentId);
        Filter likedUserFilter = Filter.equalTo("liked_user_id", userId);
        Filter filter = Filter.and(commentProfileFilter, likedUserFilter);
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).where(filter).get();
        try {
            for(QueryDocumentSnapshot doc : result.get()) {
                doc.getReference().delete().get();
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByCommentIdAndLikedById(Long commentId, String userId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter commentProfileFilter = Filter.equalTo("comment_id", commentId);
        Filter likedUserFilter = Filter.equalTo("liked_user_id", userId);
        Filter filter = Filter.and(commentProfileFilter, likedUserFilter);
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            return !result.get().isEmpty();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
    
}
