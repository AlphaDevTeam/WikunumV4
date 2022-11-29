package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashBookBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashBookBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookBalanceRepository extends JpaRepository<CashBookBalance, Long>, JpaSpecificationExecutor<CashBookBalance> {

}
