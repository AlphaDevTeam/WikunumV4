package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ConfigurationItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfigurationItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationItemsRepository extends JpaRepository<ConfigurationItems, Long>, JpaSpecificationExecutor<ConfigurationItems> {

}
