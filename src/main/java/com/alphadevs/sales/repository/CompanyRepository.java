package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.Company;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
@JaversSpringDataAuditable
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {



}
