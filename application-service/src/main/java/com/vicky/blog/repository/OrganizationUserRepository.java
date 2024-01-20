package com.vicky.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.common.dto.organization.OrganizationUserDTO.UserOrganizationRole;
import com.vicky.blog.model.OrganizationUser;

import jakarta.transaction.Transactional;

@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {

    Optional<OrganizationUser> findByOrganizationIdAndUserId(Long organizationId, String userId);

    List<OrganizationUser> findByOrganizationId(Long organizationId);

    List<OrganizationUser> findByUserId(String userId);

    List<OrganizationUser> findByOrganizationIdAndRole(Long organizationId, UserOrganizationRole role);

    @Transactional
    void deleteByOrganizationIdAndUserId(Long organizationId, String userId);

    @Transactional
    void deleteByOrganizationId(Long organizationId);
}