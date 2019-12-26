package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashPaymentVoucherSupplier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashPaymentVoucherSupplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashPaymentVoucherSupplierRepository extends JpaRepository<CashPaymentVoucherSupplier, Long>, JpaSpecificationExecutor<CashPaymentVoucherSupplier> {

}
