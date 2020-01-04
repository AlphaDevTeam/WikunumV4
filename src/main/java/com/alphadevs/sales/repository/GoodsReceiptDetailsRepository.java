package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.GoodsReceiptDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GoodsReceiptDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReceiptDetailsRepository extends JpaRepository<GoodsReceiptDetails, Long>, JpaSpecificationExecutor<GoodsReceiptDetails> {

}
