package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.StockTransfer;
import com.alphadevs.sales.repository.StockTransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link StockTransfer}.
 */
@Service
@Transactional
public class StockTransferService {

    private final Logger log = LoggerFactory.getLogger(StockTransferService.class);

    private final StockTransferRepository stockTransferRepository;

    public StockTransferService(StockTransferRepository stockTransferRepository) {
        this.stockTransferRepository = stockTransferRepository;
    }

    /**
     * Save a stockTransfer.
     *
     * @param stockTransfer the entity to save.
     * @return the persisted entity.
     */
    public StockTransfer save(StockTransfer stockTransfer) {
        log.debug("Request to save StockTransfer : {}", stockTransfer);
        return stockTransferRepository.save(stockTransfer);
    }

    /**
     * Get all the stockTransfers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StockTransfer> findAll(Pageable pageable) {
        log.debug("Request to get all StockTransfers");
        return stockTransferRepository.findAll(pageable);
    }


    /**
     * Get one stockTransfer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockTransfer> findOne(Long id) {
        log.debug("Request to get StockTransfer : {}", id);
        return stockTransferRepository.findById(id);
    }

    /**
     * Delete the stockTransfer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StockTransfer : {}", id);
        stockTransferRepository.deleteById(id);
    }
}
