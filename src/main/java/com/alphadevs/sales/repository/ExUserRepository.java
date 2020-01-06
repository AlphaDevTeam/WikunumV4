package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ExUser;
import com.alphadevs.sales.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ExUser entity.
 */
@Repository
public interface ExUserRepository extends JpaRepository<ExUser, Long>, JpaSpecificationExecutor<ExUser> {

    @Query(value = "select distinct exUser from ExUser exUser left join fetch exUser.locations left join fetch exUser.userGroups left join fetch exUser.userPermissions",
        countQuery = "select count(distinct exUser) from ExUser exUser")
    Page<ExUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct exUser from ExUser exUser left join fetch exUser.locations left join fetch exUser.userGroups left join fetch exUser.userPermissions")
    List<ExUser> findAllWithEagerRelationships();

    @Query("select exUser from ExUser exUser left join fetch exUser.locations left join fetch exUser.userGroups left join fetch exUser.userPermissions where exUser.id =:id")
    Optional<ExUser> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<ExUser> findOneByRelatedUser(User user);
}
