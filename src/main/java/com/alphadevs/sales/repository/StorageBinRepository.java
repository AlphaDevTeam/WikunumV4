package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.StorageBin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StorageBin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageBinRepository extends JpaRepository<StorageBin, Long>, JpaSpecificationExecutor<StorageBin> {

}
