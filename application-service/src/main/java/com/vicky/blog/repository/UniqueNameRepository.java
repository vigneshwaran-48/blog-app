package com.vicky.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.model.UniqueName;

@Repository
public interface UniqueNameRepository extends JpaRepository<UniqueName, Long> {
    
    Optional<UniqueName> findByEntityId(String entityId);

    boolean existsByUniqueName(String uniqueName);

    Optional<UniqueName> findByUniqueName(String uniqueName);
}
