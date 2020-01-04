package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.Location;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Location entity.
 */
@Repository
@JaversSpringDataAuditable
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    @Query(value = "select distinct location from Location location left join fetch location.configitems",
        countQuery = "select count(distinct location) from Location location")
    Page<Location> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct location from Location location left join fetch location.configitems")
    List<Location> findAllWithEagerRelationships();

    @Query("select location from Location location left join fetch location.configitems where location.id =:id")
    Optional<Location> findOneWithEagerRelationships(@Param("id") Long id);

}
