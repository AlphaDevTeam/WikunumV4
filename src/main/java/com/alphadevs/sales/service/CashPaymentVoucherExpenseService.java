package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashPaymentVoucherExpense;
import com.alphadevs.sales.repository.CashPaymentVoucherExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashPaymentVoucherExpense}.
 */
@Service
@Transactional
public class CashPaymentVoucherExpenseService {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherExpenseService.class);

    private final CashPaymentVoucherExpenseRepository cashPaymentVoucherExpenseRepository;

    public CashPaymentVoucherExpenseService(CashPaymentVoucherExpenseRepository cashPaymentVoucherExpenseRepository) {
        this.cashPaymentVoucherExpenseRepository = cashPaymentVoucherExpenseRepository;
    }

    /**
     * Save a cashPaymentVoucherExpense.
     *
     * @param cashPaymentVoucherExpense the entity to save.
     * @return the persisted entity.
     */
    public CashPaymentVoucherExpense save(CashPaymentVoucherExpense cashPaymentVoucherExpense) {
        log.debug("Request to save CashPaymentVoucherExpense : {}", cashPaymentVoucherExpense);
        return cashPaymentVoucherExpenseRepository.save(cashPaymentVoucherExpense);
    }

    /**
     * Get all the cashPaymentVoucherExpenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucherExpense> findAll(Pageable pageable) {
        log.debug("Request to get all CashPaymentVoucherExpenses");
        return cashPaymentVoucherExpenseRepository.findAll(pageable);
    }


    /**
     * Get one cashPaymentVoucherExpense by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashPaymentVoucherExpense> findOne(Long id) {
        log.debug("Request to get CashPaymentVoucherExpense : {}", id);
        return cashPaymentVoucherExpenseRepository.findById(id);
    }

    /**
     * Delete the cashPaymentVoucherExpense by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashPaymentVoucherExpense : {}", id);
        cashPaymentVoucherExpenseRepository.deleteById(id);
    }
}
