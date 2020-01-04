package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ExpenseAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExpenseAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseAccountRepository extends JpaRepository<ExpenseAccount, Long>, JpaSpecificationExecutor<ExpenseAccount> {

}
