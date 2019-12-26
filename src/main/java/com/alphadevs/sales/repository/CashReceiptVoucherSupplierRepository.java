package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashReceiptVoucherSupplier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashReceiptVoucherSupplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashReceiptVoucherSupplierRepository extends JpaRepository<CashReceiptVoucherSupplier, Long>, JpaSpecificationExecutor<CashReceiptVoucherSupplier> {

}
