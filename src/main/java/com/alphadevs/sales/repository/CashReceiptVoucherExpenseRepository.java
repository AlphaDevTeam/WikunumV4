package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashReceiptVoucherExpense;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashReceiptVoucherExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashReceiptVoucherExpenseRepository extends JpaRepository<CashReceiptVoucherExpense, Long>, JpaSpecificationExecutor<CashReceiptVoucherExpense> {

}
