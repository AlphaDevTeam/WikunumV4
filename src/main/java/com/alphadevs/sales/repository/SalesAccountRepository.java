package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.SalesAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SalesAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesAccountRepository extends JpaRepository<SalesAccount, Long>, JpaSpecificationExecutor<SalesAccount> {

}
