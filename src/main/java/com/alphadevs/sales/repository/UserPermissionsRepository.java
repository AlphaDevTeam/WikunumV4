package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.UserPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserPermissions entity.
 */
@Repository
public interface UserPermissionsRepository extends JpaRepository<UserPermissions, Long>, JpaSpecificationExecutor<UserPermissions> {

    @Query(value = "select distinct userPermissions from UserPermissions userPermissions left join fetch userPermissions.menuItems",
        countQuery = "select count(distinct userPermissions) from UserPermissions userPermissions")
    Page<UserPermissions> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userPermissions from UserPermissions userPermissions left join fetch userPermissions.menuItems")
    List<UserPermissions> findAllWithEagerRelationships();

    @Query("select userPermissions from UserPermissions userPermissions left join fetch userPermissions.menuItems where userPermissions.id =:id")
    Optional<UserPermissions> findOneWithEagerRelationships(@Param("id") Long id);

}
