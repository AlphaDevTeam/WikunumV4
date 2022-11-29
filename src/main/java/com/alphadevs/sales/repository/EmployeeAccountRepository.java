package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.EmployeeAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EmployeeAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, Long>, JpaSpecificationExecutor<EmployeeAccount> {

}
