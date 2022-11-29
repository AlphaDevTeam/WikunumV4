package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.LicenseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LicenseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicenseTypeRepository extends JpaRepository<LicenseType, Long>, JpaSpecificationExecutor<LicenseType> {

}
