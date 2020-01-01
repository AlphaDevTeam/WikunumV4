package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.DocumentType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the DocumentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long>, JpaSpecificationExecutor<DocumentType> {

    //Custom Code
    List<DocumentType> findByDocumentType(String documentType);
    List<DocumentType> findByDocumentTypeCode(String documentTypeCode);


}
