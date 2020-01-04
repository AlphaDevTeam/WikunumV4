package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashBookBalance;
import com.alphadevs.sales.repository.CashBookBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashBookBalance}.
 */
@Service
@Transactional
public class CashBookBalanceService {

    private final Logger log = LoggerFactory.getLogger(CashBookBalanceService.class);

    private final CashBookBalanceRepository cashBookBalanceRepository;

    public CashBookBalanceService(CashBookBalanceRepository cashBookBalanceRepository) {
        this.cashBookBalanceRepository = cashBookBalanceRepository;
    }

    /**
     * Save a cashBookBalance.
     *
     * @param cashBookBalance the entity to save.
     * @return the persisted entity.
     */
    public CashBookBalance save(CashBookBalance cashBookBalance) {
        log.debug("Request to save CashBookBalance : {}", cashBookBalance);
        return cashBookBalanceRepository.save(cashBookBalance);
    }

    /**
     * Get all the cashBookBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashBookBalance> findAll(Pageable pageable) {
        log.debug("Request to get all CashBookBalances");
        return cashBookBalanceRepository.findAll(pageable);
    }


    /**
     * Get one cashBookBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashBookBalance> findOne(Long id) {
        log.debug("Request to get CashBookBalance : {}", id);
        return cashBookBalanceRepository.findById(id);
    }

    /**
     * Delete the cashBookBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashBookBalance : {}", id);
        cashBookBalanceRepository.deleteById(id);
    }
}
