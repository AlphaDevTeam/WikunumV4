package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.InvoiceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvoiceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, Long>, JpaSpecificationExecutor<InvoiceDetails> {

}
