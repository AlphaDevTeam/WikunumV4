package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.PaymentTypes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PaymentTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTypesRepository extends JpaRepository<PaymentTypes, Long>, JpaSpecificationExecutor<PaymentTypes> {

}
