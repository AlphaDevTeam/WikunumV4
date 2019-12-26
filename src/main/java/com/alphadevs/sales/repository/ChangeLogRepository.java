package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ChangeLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChangeLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, JpaSpecificationExecutor<ChangeLog> {

}
