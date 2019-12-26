package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.PaymentTypeBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentTypeBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTypeBalanceRepository extends JpaRepository<PaymentTypeBalance, Long>, JpaSpecificationExecutor<PaymentTypeBalance> {

}
