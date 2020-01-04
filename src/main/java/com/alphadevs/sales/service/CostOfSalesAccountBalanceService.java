package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CostOfSalesAccountBalance;
import com.alphadevs.sales.repository.CostOfSalesAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CostOfSalesAccountBalance}.
 */
@Service
@Transactional
public class CostOfSalesAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(CostOfSalesAccountBalanceService.class);

    private final CostOfSalesAccountBalanceRepository costOfSalesAccountBalanceRepository;

    public CostOfSalesAccountBalanceService(CostOfSalesAccountBalanceRepository costOfSalesAccountBalanceRepository) {
        this.costOfSalesAccountBalanceRepository = costOfSalesAccountBalanceRepository;
    }

    /**
     * Save a costOfSalesAccountBalance.
     *
     * @param costOfSalesAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public CostOfSalesAccountBalance save(CostOfSalesAccountBalance costOfSalesAccountBalance) {
        log.debug("Request to save CostOfSalesAccountBalance : {}", costOfSalesAccountBalance);
        return costOfSalesAccountBalanceRepository.save(costOfSalesAccountBalance);
    }

    /**
     * Get all the costOfSalesAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CostOfSalesAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all CostOfSalesAccountBalances");
        return costOfSalesAccountBalanceRepository.findAll(pageable);
    }


    /**
     * Get one costOfSalesAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CostOfSalesAccountBalance> findOne(Long id) {
        log.debug("Request to get CostOfSalesAccountBalance : {}", id);
        return costOfSalesAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the costOfSalesAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CostOfSalesAccountBalance : {}", id);
        costOfSalesAccountBalanceRepository.deleteById(id);
    }
}
