package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.DocumentNumberConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DocumentNumberConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentNumberConfigRepository extends JpaRepository<DocumentNumberConfig, Long>, JpaSpecificationExecutor<DocumentNumberConfig> {

}
