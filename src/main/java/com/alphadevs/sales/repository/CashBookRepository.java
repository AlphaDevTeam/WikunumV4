package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.CashBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long>, JpaSpecificationExecutor<CashBook> {

}
