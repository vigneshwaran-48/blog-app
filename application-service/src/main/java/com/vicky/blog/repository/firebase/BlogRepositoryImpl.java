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
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.vicky.blog.model.Blog;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.BlogRepository;
import com.vicky.blog.repository.ProfileIdRepository;
import com.vicky.blog.repository.UserRepository;
import com.vicky.blog.repository.firebase.model.BlogModal;

@Repository
@Profile("prod")
public class BlogRepositoryImpl implements BlogRepository {

    private static final String COLLECTION_NAME = "blog";
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogRepositoryImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileIdRepository profileIdRepository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Blog> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Blog> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Blog> entities) {
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
    public Blog getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Blog getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Blog getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends Blog> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Blog> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Blog> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<Blog> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Blog> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public <S extends Blog> S save(S entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        Long id = entity.getId();
        if(id == null) {
            id = FirebaseUtil.getNextOrderedId(COLLECTION_NAME);
            if(id < 0) {
                return null;
            }
            entity.setId(id);
        }
        BlogModal blogModal = BlogModal.build(entity);
        try {
            firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).set(blogModal).get();
            return entity;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<Blog> findById(Long id) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> result = firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).get();
        if (result.isDone()) {
            return Optional.empty();
        }
        try {
            DocumentSnapshot snapshot = result.get();
            BlogModal blogModal = snapshot.toObject(BlogModal.class);
            if(blogModal == null) {
                return Optional.empty();
            }
            Blog blog = blogModal.toEntity();
            User owner = userRepository.findById(blogModal.getOwner_id()).get();
            blog.setOwner(owner);
            if(blog.isPublised()) {
                ProfileId publishedProfile = profileIdRepository.findById(blogModal.getPublished_at()).get();
                blog.setPublishedAt(publishedProfile);
            }
            return Optional.of(blog);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void delete(Blog entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Blog> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<Blog> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Blog> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Blog> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Blog> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Blog> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Blog> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Blog, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public Optional<Blog> findByOwnerIdAndId(String userId, Long id) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter blogFilter = Filter.equalTo("id", id);
        Filter ownerFilter = Filter.equalTo("owner_id", userId);
        Filter filter = Filter.and(ownerFilter, blogFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<BlogModal> blogModals = snapshot.toObjects(BlogModal.class);
            if (blogModals.isEmpty()) {
                return Optional.empty();
            }
            Blog blog = blogModals.get(0).toEntity();
            User owner = userRepository.findById(blogModals.get(0).getOwner_id()).get();
            blog.setOwner(owner);
            if(blog.isPublised()) {
                ProfileId publishedProfile = profileIdRepository.findById(blogModals.get(0).getPublished_at()).get();
                blog.setPublishedAt(publishedProfile);
            }
            return Optional.of(blog);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Blog> findByOwnerId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).whereEqualTo("owner_id", userId).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<BlogModal> blogModals = snapshot.toObjects(BlogModal.class);
            if (blogModals.isEmpty()) {
                return List.of();
            }
            List<Blog> blogs = new ArrayList<>();
            for (BlogModal blogModal : blogModals) {
                Blog blog = blogModal.toEntity();
                blog.setOwner(user.get());
                if(blog.isPublised()) {
                    ProfileId publishedProfile = profileIdRepository.findById(blogModal.getPublished_at()).get();
                    blog.setPublishedAt(publishedProfile);
                }
                blogs.add(blog);
            }
            return blogs;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public Optional<Blog> findByIdAndPublishedAtProfileId(Long id, String profileId) {
        Optional<ProfileId> profile = profileIdRepository.findByProfileId(profileId);
        if(profile.isEmpty()) {
            return Optional.empty();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        Filter blogFilter = Filter.equalTo("id", id);
        Filter profileFilter = Filter.equalTo("published_at", profile.get().getId());
        Filter filter = Filter.and(blogFilter, profileFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<BlogModal> blogModals = snapshot.toObjects(BlogModal.class);
            if (blogModals.isEmpty()) {
                return Optional.empty();
            }
            Blog blog = blogModals.get(0).toEntity();
            User owner = userRepository.findById(blogModals.get(0).getOwner_id()).get();
            blog.setOwner(owner);
            if(blog.isPublised()) {
                ProfileId publishedProfile = profileIdRepository.findById(blogModals.get(0).getPublished_at()).get();
                blog.setPublishedAt(publishedProfile);
            }
            return Optional.of(blog);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Blog> findByPublishedAtProfileId(String profileId) {
        Optional<ProfileId> profile = profileIdRepository.findByProfileId(profileId);
        if (profile.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME)
                                                    .whereEqualTo("published_at", profile.get().getId()).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<BlogModal> blogModals = snapshot.toObjects(BlogModal.class);
            if (blogModals.isEmpty()) {
                return List.of();
            }
            List<Blog> blogs = new ArrayList<>();
            for (BlogModal blogModal : blogModals) {
                Blog blog = blogModal.toEntity();
                User user = userRepository.findById(blogModal.getOwner_id()).get();
                blog.setOwner(user);
                blog.setPublishedAt(profile.get());
                blogs.add(blog);
            }
            return blogs;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void deleteByOwnerIdAndId(String userId, Long id) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter userFilter = Filter.equalTo("owner_id", userId);
        Filter blogFilter = Filter.equalTo("id", id);
        Filter filter = Filter.and(blogFilter, userFilter);
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
    
}
