package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.PurchaseOrderDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseOrderDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderDetailsRepository extends JpaRepository<PurchaseOrderDetails, Long>, JpaSpecificationExecutor<PurchaseOrderDetails> {

}
