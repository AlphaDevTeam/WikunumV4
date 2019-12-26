package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ItemAddOns;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemAddOns entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemAddOnsRepository extends JpaRepository<ItemAddOns, Long>, JpaSpecificationExecutor<ItemAddOns> {

}
