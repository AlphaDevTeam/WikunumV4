package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.EmployeeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EmployeeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long>, JpaSpecificationExecutor<EmployeeType> {

}
