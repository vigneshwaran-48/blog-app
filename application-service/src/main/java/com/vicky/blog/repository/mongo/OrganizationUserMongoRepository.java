package com.vicky.blog.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.model.OrganizationUser;

public interface OrganizationUserMongoRepository extends MongoRepository<OrganizationUser, String> {
    
    Optional<OrganizationUser> findByOrganizationIdAndUserId(String organizationId, String userId);

    List<OrganizationUser> findByOrganizationId(String organizationId);

    List<OrganizationUser> findByUserId(String userId);

    List<OrganizationUser> findByOrganizationIdAndRole(String organizationId, UserOrganizationRole role);

    void deleteByOrganizationIdAndUserId(String organizationId, String userId);

    void deleteByOrganizationId(String organizationId);
}
