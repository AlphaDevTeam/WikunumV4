package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.SalesAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SalesAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesAccountBalanceRepository extends JpaRepository<SalesAccountBalance, Long>, JpaSpecificationExecutor<SalesAccountBalance> {

}
