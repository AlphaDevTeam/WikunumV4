package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.GoodsReceipt;
import com.alphadevs.sales.repository.GoodsReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link GoodsReceipt}.
 */
@Service
@Transactional
public class GoodsReceiptService {

    private final Logger log = LoggerFactory.getLogger(GoodsReceiptService.class);

    private final GoodsReceiptRepository goodsReceiptRepository;

    public GoodsReceiptService(GoodsReceiptRepository goodsReceiptRepository) {
        this.goodsReceiptRepository = goodsReceiptRepository;
    }

    /**
     * Save a goodsReceipt.
     *
     * @param goodsReceipt the entity to save.
     * @return the persisted entity.
     */
    public GoodsReceipt save(GoodsReceipt goodsReceipt) {
        log.debug("Request to save GoodsReceipt : {}", goodsReceipt);
        return goodsReceiptRepository.save(goodsReceipt);
    }

    /**
     * Get all the goodsReceipts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GoodsReceipt> findAll(Pageable pageable) {
        log.debug("Request to get all GoodsReceipts");
        return goodsReceiptRepository.findAll(pageable);
    }


    /**
     * Get one goodsReceipt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GoodsReceipt> findOne(Long id) {
        log.debug("Request to get GoodsReceipt : {}", id);
        return goodsReceiptRepository.findById(id);
    }

    /**
     * Delete the goodsReceipt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GoodsReceipt : {}", id);
        goodsReceiptRepository.deleteById(id);
    }
}
