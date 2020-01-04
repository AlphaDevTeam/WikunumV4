package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.EmployeeAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EmployeeAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeAccountBalanceRepository extends JpaRepository<EmployeeAccountBalance, Long>, JpaSpecificationExecutor<EmployeeAccountBalance> {

}
