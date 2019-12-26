package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashPaymentVoucherCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashPaymentVoucherCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashPaymentVoucherCustomerRepository extends JpaRepository<CashPaymentVoucherCustomer, Long>, JpaSpecificationExecutor<CashPaymentVoucherCustomer> {

}
