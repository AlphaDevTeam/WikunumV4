package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.GoodsReceipt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GoodsReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long>, JpaSpecificationExecutor<GoodsReceipt> {

}
