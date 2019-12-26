package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashReceiptVoucherCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashReceiptVoucherCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashReceiptVoucherCustomerRepository extends JpaRepository<CashReceiptVoucherCustomer, Long>, JpaSpecificationExecutor<CashReceiptVoucherCustomer> {

}
