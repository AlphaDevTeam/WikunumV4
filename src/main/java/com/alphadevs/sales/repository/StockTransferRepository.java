package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.StockTransfer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StockTransfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long>, JpaSpecificationExecutor<StockTransfer> {

}
