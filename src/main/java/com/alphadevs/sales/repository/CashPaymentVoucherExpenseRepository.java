package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashPaymentVoucherExpense;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashPaymentVoucherExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashPaymentVoucherExpenseRepository extends JpaRepository<CashPaymentVoucherExpense, Long>, JpaSpecificationExecutor<CashPaymentVoucherExpense> {

}
