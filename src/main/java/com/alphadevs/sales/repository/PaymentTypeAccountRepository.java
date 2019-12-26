package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.PaymentTypeAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentTypeAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTypeAccountRepository extends JpaRepository<PaymentTypeAccount, Long>, JpaSpecificationExecutor<PaymentTypeAccount> {

}
