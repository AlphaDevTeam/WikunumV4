package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.DocumentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the DocumentHistory entity.
 */
@Repository
public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long>, JpaSpecificationExecutor<DocumentHistory> {

    @Query(value = "select distinct documentHistory from DocumentHistory documentHistory left join fetch documentHistory.changeLogs",
        countQuery = "select count(distinct documentHistory) from DocumentHistory documentHistory")
    Page<DocumentHistory> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct documentHistory from DocumentHistory documentHistory left join fetch documentHistory.changeLogs")
    List<DocumentHistory> findAllWithEagerRelationships();

    @Query("select documentHistory from DocumentHistory documentHistory left join fetch documentHistory.changeLogs where documentHistory.id =:id")
    Optional<DocumentHistory> findOneWithEagerRelationships(@Param("id") Long id);

}
