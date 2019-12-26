package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.ExpenseAccountBalance;
import com.alphadevs.sales.repository.ExpenseAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExpenseAccountBalance}.
 */
@Service
@Transactional
public class ExpenseAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountBalanceService.class);

    private final ExpenseAccountBalanceRepository expenseAccountBalanceRepository;

    public ExpenseAccountBalanceService(ExpenseAccountBalanceRepository expenseAccountBalanceRepository) {
        this.expenseAccountBalanceRepository = expenseAccountBalanceRepository;
    }

    /**
     * Save a expenseAccountBalance.
     *
     * @param expenseAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public ExpenseAccountBalance save(ExpenseAccountBalance expenseAccountBalance) {
        log.debug("Request to save ExpenseAccountBalance : {}", expenseAccountBalance);
        return expenseAccountBalanceRepository.save(expenseAccountBalance);
    }

    /**
     * Get all the expenseAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseAccountBalances");
        return expenseAccountBalanceRepository.findAll(pageable);
    }


    /**
     * Get one expenseAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseAccountBalance> findOne(Long id) {
        log.debug("Request to get ExpenseAccountBalance : {}", id);
        return expenseAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the expenseAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpenseAccountBalance : {}", id);
        expenseAccountBalanceRepository.deleteById(id);
    }
}
