package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.Location;
import com.alphadevs.sales.domain.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Products entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsRepository extends JpaSpecificationExecutor<Products>,JpaRepository<Products, Long> {
    Page<Products> findAllByLocationIn(List<Location> locations, Pageable page);
}
