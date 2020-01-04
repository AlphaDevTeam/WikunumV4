package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ExpenseAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExpenseAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseAccountBalanceRepository extends JpaRepository<ExpenseAccountBalance, Long>, JpaSpecificationExecutor<ExpenseAccountBalance> {

}
