package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.ItemBinCard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemBinCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemBinCardRepository extends JpaRepository<ItemBinCard, Long>, JpaSpecificationExecutor<ItemBinCard> {

}
