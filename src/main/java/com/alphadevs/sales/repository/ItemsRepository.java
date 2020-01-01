package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Items entity.
 */
@Repository
public interface ItemsRepository extends JpaRepository<Items, Long>, JpaSpecificationExecutor<Items> {

    @Query(value = "select distinct items from Items items left join fetch items.addons",
        countQuery = "select count(distinct items) from Items items")
    Page<Items> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct items from Items items left join fetch items.addons")
    List<Items> findAllWithEagerRelationships();

    @Query("select items from Items items left join fetch items.addons where items.id =:id")
    Optional<Items> findOneWithEagerRelationships(@Param("id") Long id);

}
