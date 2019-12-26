package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.SupplierAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SupplierAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierAccountRepository extends JpaRepository<SupplierAccount, Long>, JpaSpecificationExecutor<SupplierAccount> {

}
