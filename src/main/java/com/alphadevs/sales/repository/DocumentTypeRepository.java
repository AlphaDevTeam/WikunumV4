package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.DocumentType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DocumentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long>, JpaSpecificationExecutor<DocumentType> {

}