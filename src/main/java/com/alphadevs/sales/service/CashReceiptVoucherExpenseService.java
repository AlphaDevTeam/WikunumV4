package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashReceiptVoucherExpense;
import com.alphadevs.sales.repository.CashReceiptVoucherExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashReceiptVoucherExpense}.
 */
@Service
@Transactional
public class CashReceiptVoucherExpenseService {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherExpenseService.class);

    private final CashReceiptVoucherExpenseRepository cashReceiptVoucherExpenseRepository;

    public CashReceiptVoucherExpenseService(CashReceiptVoucherExpenseRepository cashReceiptVoucherExpenseRepository) {
        this.cashReceiptVoucherExpenseRepository = cashReceiptVoucherExpenseRepository;
    }

    /**
     * Save a cashReceiptVoucherExpense.
     *
     * @param cashReceiptVoucherExpense the entity to save.
     * @return the persisted entity.
     */
    public CashReceiptVoucherExpense save(CashReceiptVoucherExpense cashReceiptVoucherExpense) {
        log.debug("Request to save CashReceiptVoucherExpense : {}", cashReceiptVoucherExpense);
        return cashReceiptVoucherExpenseRepository.save(cashReceiptVoucherExpense);
    }

    /**
     * Get all the cashReceiptVoucherExpenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashReceiptVoucherExpense> findAll(Pageable pageable) {
        log.debug("Request to get all CashReceiptVoucherExpenses");
        return cashReceiptVoucherExpenseRepository.findAll(pageable);
    }


    /**
     * Get one cashReceiptVoucherExpense by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashReceiptVoucherExpense> findOne(Long id) {
        log.debug("Request to get CashReceiptVoucherExpense : {}", id);
        return cashReceiptVoucherExpenseRepository.findById(id);
    }

    /**
     * Delete the cashReceiptVoucherExpense by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashReceiptVoucherExpense : {}", id);
        cashReceiptVoucherExpenseRepository.deleteById(id);
    }
}
