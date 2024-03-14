package com.vicky.blog.repository.firebase;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.model.ProfileId;
import com.vicky.blog.repository.ProfileIdRepository;

@Repository
@Profile("prod")
public class ProfileIdRepositorImpl implements ProfileIdRepository {

    private static final String COLLECTION_NAME = "profile_id";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileIdRepositorImpl.class);

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends ProfileId> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends ProfileId> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<ProfileId> entities) {
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
    public ProfileId getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public ProfileId getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public ProfileId getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends ProfileId> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends ProfileId> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends ProfileId> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<ProfileId> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<ProfileId> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public <S extends ProfileId> S save(S entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        long id = FirebaseUtil.getUniqueLong();
        try {
            firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).set(entity).get();
            entity.setId(id);
            return entity;
        } 
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } 
        catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<ProfileId> findById(Long id) {
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
    public void delete(ProfileId entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends ProfileId> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<ProfileId> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<ProfileId> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends ProfileId> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends ProfileId> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends ProfileId> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends ProfileId> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends ProfileId, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public Optional<ProfileId> findByEntityId(String entityId) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).whereEqualTo("entity_id", entityId).limit(1).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<ProfileId> profileIds = snapshot.toObjects(ProfileId.class);
            if (profileIds.isEmpty()) {
                return Optional.empty();
            }
            String type = (String) snapshot.getDocuments().get(0).get("type");
            ProfileId profileId = profileIds.get(0);
            profileId.setType(ProfileType.valueOf(type));
            return Optional.of(profileId);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByProfileId(String profileId) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).whereEqualTo("profile_id", profileId).limit(1).get();
        try {
            List<ProfileId> profileIds = result.get().toObjects(ProfileId.class);
            return !profileIds.isEmpty();
        } 
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } 
        catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Optional<ProfileId> findByProfileId(String profileId) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).whereEqualTo("profile_id", profileId).limit(1).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<ProfileId> profileIds = snapshot.toObjects(ProfileId.class);
            if (profileIds.isEmpty()) {
                return Optional.empty();
            }
            String type = (String) snapshot.getDocuments().get(0).get("type");
            ProfileId profileIdModel = profileIds.get(0);
            profileIdModel.setType(ProfileType.valueOf(type));
            return Optional.of(profileIdModel);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByProfileIdAndEntityId(String profileId, String entityId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter profileFilter = Filter.equalTo("profile_id", profileId);
        Filter entityProfile = Filter.equalTo("entity_id", entityId);
        Filter filter = Filter.and(profileFilter, entityProfile);
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).where(filter).limit(1).get();
        try {
            result.get().forEach(doc -> {
                doc.getReference().delete();
            });
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
