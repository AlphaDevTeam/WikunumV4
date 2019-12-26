package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

}
