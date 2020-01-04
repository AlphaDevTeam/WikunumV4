package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.GoodsReceiptDetails;
import com.alphadevs.sales.repository.GoodsReceiptDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link GoodsReceiptDetails}.
 */
@Service
@Transactional
public class GoodsReceiptDetailsService {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptDetailsService.class);

    private final GoodsReceiptDetailsRepository goodsReceiptDetailsRepository;

    public GoodsReceiptDetailsService(GoodsReceiptDetailsRepository goodsReceiptDetailsRepository) {
        this.goodsReceiptDetailsRepository = goodsReceiptDetailsRepository;
    }

    /**
     * Save a goodsReceiptDetails.
     *
     * @param goodsReceiptDetails the entity to save.
     * @return the persisted entity.
     */
    public GoodsReceiptDetails save(GoodsReceiptDetails goodsReceiptDetails) {
        log.debug("Request to save GoodsReceiptDetails : {}", goodsReceiptDetails);
        return goodsReceiptDetailsRepository.save(goodsReceiptDetails);
    }

    /**
     * Get all the goodsReceiptDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GoodsReceiptDetails> findAll(Pageable pageable) {
        log.debug("Request to get all GoodsReceiptDetails");
        return goodsReceiptDetailsRepository.findAll(pageable);
    }


    /**
     * Get one goodsReceiptDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoodsReceiptDetails> findOne(Long id) {
        log.debug("Request to get GoodsReceiptDetails : {}", id);
        return goodsReceiptDetailsRepository.findById(id);
    }

    /**
     * Delete the goodsReceiptDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoodsReceiptDetails : {}", id);
        goodsReceiptDetailsRepository.deleteById(id);
    }
}
