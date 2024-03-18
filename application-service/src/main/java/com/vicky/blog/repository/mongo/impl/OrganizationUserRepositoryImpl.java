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
import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.model.Organization;
import com.vicky.blog.model.OrganizationUser;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.OrganizationRepository;
import com.vicky.blog.repository.OrganizationUserRepository;
import com.vicky.blog.repository.UserRepository;
import com.vicky.blog.repository.firebase.model.OrganizationUserModal;

@Repository
@Profile("prod")
public class OrganizationUserRepositoryImpl implements OrganizationUserRepository {

    private static final String COLLECTION_NAME = "organization_user";
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationUserRepositoryImpl.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends OrganizationUser> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends OrganizationUser> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<OrganizationUser> entities) {
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
    public OrganizationUser getOne(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public OrganizationUser getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public OrganizationUser getReferenceById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
    }

    @Override
    public <S extends OrganizationUser> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends OrganizationUser> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends OrganizationUser> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<OrganizationUser> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<OrganizationUser> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public <S extends OrganizationUser> S save(S entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        Long id = entity.getId();
        if(id == null) {
            id = FirebaseUtil.getNextOrderedId(COLLECTION_NAME);
            if(id < 0) {
                return null;
            }
            entity.setId(id);
        }
        OrganizationUserModal organizationUserModal = OrganizationUserModal.build(entity);
        try {
            firestore.collection(COLLECTION_NAME).document(String.valueOf(id)).set(organizationUserModal).get();
            return entity;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Optional<OrganizationUser> findById(Long id) {
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
    public void delete(OrganizationUser entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends OrganizationUser> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public List<OrganizationUser> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<OrganizationUser> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends OrganizationUser> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends OrganizationUser> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends OrganizationUser> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends OrganizationUser> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends OrganizationUser, R> R findBy(Example<S> example,
            Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public Optional<OrganizationUser> findByOrganizationIdAndUserId(Long organizationId, String userId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        Optional<User> user = userRepository.findById(userId);
        if (organization.isEmpty() || user.isEmpty()) {
            return Optional.empty();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        Filter organizationFilter = Filter.equalTo("organization_id", organizationId);
        Filter userFilter = Filter.equalTo("user_id", userId);
        Filter filter = Filter.and(organizationFilter, userFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).limit(1).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<OrganizationUserModal> organizationUsers = snapshot.toObjects(OrganizationUserModal.class);
            if (organizationUsers.isEmpty()) {
                return Optional.empty();
            }
            String role = (String) snapshot.getDocuments().get(0).get("role");

            OrganizationUserModal orgModal = organizationUsers.get(0);
            OrganizationUser orgUser = orgModal.toEntity();
            orgUser.setOrganization(organization.get());
            orgUser.setUser(user.get());
            orgUser.setRole(UserOrganizationRole.valueOf(role));

            return Optional.of(orgUser);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<OrganizationUser> findByOrganizationId(Long organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).whereEqualTo("organization_id", organizationId).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<OrganizationUserModal> organizationUsers = snapshot.toObjects(OrganizationUserModal.class);
            if (organizationUsers.isEmpty()) {
                return List.of();
            }
            List<OrganizationUser> orgUsers = new ArrayList<>();
            for (int i = 0; i < snapshot.getDocuments().size(); i++) {
                String role = (String) snapshot.getDocuments().get(i).get("role");

                OrganizationUser organizationUser = organizationUsers.get(i).toEntity();
                organizationUser.setRole(UserOrganizationRole.valueOf(role));
                organizationUser.setOrganization(organization.get());
                User user = userRepository.findById(organizationUsers.get(i).getUser_id()).get();
                organizationUser.setUser(user);
                orgUsers.add(organizationUser);
            }
            return orgUsers;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public List<OrganizationUser> findByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).whereEqualTo("user_id", userId).get();
        try {
            QuerySnapshot snapshot = result.get();
            List<OrganizationUserModal> organizationUsers = snapshot.toObjects(OrganizationUserModal.class);
            if (organizationUsers.isEmpty()) {
                return List.of();
            }
            List<OrganizationUser> orgUsers = new ArrayList<>();
            for (int i = 0; i < snapshot.getDocuments().size(); i++) {
                String role = (String) snapshot.getDocuments().get(i).get("role");

                OrganizationUserModal organizationUserModal = organizationUsers.get(i);
                Organization organization =
                        organizationRepository.findById(organizationUserModal.getOrganization_id()).get();
                OrganizationUser organizationUser = organizationUserModal.toEntity();
                organizationUser.setOrganization(organization);
                organizationUser.setRole(UserOrganizationRole.valueOf(role));
                organizationUser.setUser(user.get());
                orgUsers.add(organizationUser);
            }
            return orgUsers;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public List<OrganizationUser> findByOrganizationIdAndRole(Long organizationId, UserOrganizationRole role) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return List.of();
        }
        Firestore firestore = FirestoreClient.getFirestore();
        Filter organizationFilter = Filter.equalTo("organization_id", organizationId);
        Filter roleFilter = Filter.equalTo("role", role.toString());
        Filter filter = Filter.and(organizationFilter, roleFilter);
        ApiFuture<QuerySnapshot> result = firestore.collection(COLLECTION_NAME).where(filter).get();

        try {
            QuerySnapshot snapshot = result.get();
            List<OrganizationUserModal> organizationUsers = snapshot.toObjects(OrganizationUserModal.class);
            if (organizationUsers.isEmpty()) {
                return List.of();
            }
            List<OrganizationUser> orgUsers = new ArrayList<>();
            for (int i = 0; i < snapshot.getDocuments().size(); i++) {
                String orgUserRole = (String) snapshot.getDocuments().get(i).get("role");

                OrganizationUser organizationUser = organizationUsers.get(i).toEntity();
                organizationUser.setRole(UserOrganizationRole.valueOf(orgUserRole));
                organizationUser.setOrganization(organization.get());
                User user = userRepository.findById(organizationUsers.get(i).getUser_id()).get();
                organizationUser.setUser(user);
                orgUsers.add(organizationUser);
            }
            return orgUsers;
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void deleteByOrganizationIdAndUserId(Long organizationId, String userId) {
        Firestore firestore = FirestoreClient.getFirestore();
        Filter organizationFilter = Filter.equalTo("organization_id", organizationId);
        Filter userFilter = Filter.equalTo("user_id", userId);
        Filter filter = Filter.and(organizationFilter, userFilter);
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
    public void deleteByOrganizationId(Long organizationId) {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> result =
                firestore.collection(COLLECTION_NAME).whereEqualTo("organization_id", organizationId).get();
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