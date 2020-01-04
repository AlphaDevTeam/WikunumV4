package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CostOfSalesAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CostOfSalesAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostOfSalesAccountBalanceRepository extends JpaRepository<CostOfSalesAccountBalance, Long>, JpaSpecificationExecutor<CostOfSalesAccountBalance> {

}
