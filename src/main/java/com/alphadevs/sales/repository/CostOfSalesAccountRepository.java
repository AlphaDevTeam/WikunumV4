package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CostOfSalesAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CostOfSalesAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostOfSalesAccountRepository extends JpaRepository<CostOfSalesAccount, Long>, JpaSpecificationExecutor<CostOfSalesAccount> {

}
