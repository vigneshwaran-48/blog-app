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
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.BlogLike;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.BlogLikeRepository;
import com.vicky.blog.repository.BlogRepository;
import com.vicky.blog.repository.UserRepository;
import com.vicky.blog.repository.firebase.model.BlogLikeModal;

@Repository
@Profile("prod")
public class BlogLikeRepositoryImpl implements BlogLikeRepository {

    private static final String COLLECTION_NAME = "blog_like";
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogLikeRepositoryImpl.class);

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
    public <S extends BlogLike> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends BlogLike> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<BlogLike> entities) {
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
    public BlogLike getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public BlogLike getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public BlogLike getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends BlogLike> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends BlogLike> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends BlogLike> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<BlogLike> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<BlogLike> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public <S extends BlogLike> S save(S entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        long id = FirebaseUtil.getUniqueLong();
        entity.setId(id);
        BlogLikeModal blogLikeModal = BlogLikeModal.build(entity);
        try {
            firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).set(blogLikeModal).get();
            return entity;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<BlogLike> findById(Long id) {
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
    public void delete(BlogLike entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends BlogLike> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<BlogLike> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<BlogLike> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends BlogLike> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends BlogLike> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends BlogLike> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends BlogLike> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends BlogLike, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public void deleteByBlogIdAndLikedById(Long blogId, String userId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter likedUserFilter = Filter.equalTo("liked_user_id", userId);
        Filter blogFilter = Filter.equalTo("blog_id", blogId);
        Filter filter = Filter.and(blogFilter, likedUserFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            for (QueryDocumentSnapshot doc : result.get()) {
                doc.getReference().delete().get();
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public List<BlogLike> findByBlogId(Long blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME)
                                                    .whereEqualTo("blog_id", blogId).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<BlogLikeModal> blogLikeModals = snapshot.toObjects(BlogLikeModal.class);
            if (blogLikeModals.isEmpty()) {
                return List.of();
            }
            List<BlogLike> blogLikes = new ArrayList<>();
            for (BlogLikeModal blogLikeModal : blogLikeModals) {
                BlogLike blogLike = blogLikeModal.toEntity();
                User user = userRepository.findById(blogLikeModal.getLiked_user_id()).get();
                blogLike.setBlog(blog.get());
                blogLike.setLikedBy(user);
                blogLikes.add(blogLike);
            }
            return blogLikes;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<BlogLike> findByBlogIdAndLikedById(Long blogId, String userId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter blogFilter = Filter.equalTo("blog_id", blogId);
        Filter likedUserFilter = Filter.equalTo("liked_user_id", userId);
        Filter filter = Filter.and(blogFilter, likedUserFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<BlogLikeModal> blogLikeModals = snapshot.toObjects(BlogLikeModal.class);
            if (blogLikeModals.isEmpty()) {
                return Optional.empty();
            }
            BlogLike blogLike = blogLikeModals.get(0).toEntity();
            Blog blog = blogRepository.findById(blogId).get();
            User likedByUser = userRepository.findById(blogLikeModals.get(0).getLiked_user_id()).get();
            blogLike.setLikedBy(likedByUser);
            blogLike.setBlog(blog);
            return Optional.of(blogLike);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
    
}
