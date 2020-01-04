package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.ExpenseAccount;
import com.alphadevs.sales.repository.ExpenseAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExpenseAccount}.
 */
@Service
@Transactional
public class ExpenseAccountService {

    private final Logger log = LoggerFactory.getLogger(ExpenseAccountService.class);

    private final ExpenseAccountRepository expenseAccountRepository;

    public ExpenseAccountService(ExpenseAccountRepository expenseAccountRepository) {
        this.expenseAccountRepository = expenseAccountRepository;
    }

    /**
     * Save a expenseAccount.
     *
     * @param expenseAccount the entity to save.
     * @return the persisted entity.
     */
    public ExpenseAccount save(ExpenseAccount expenseAccount) {
        log.debug("Request to save ExpenseAccount : {}", expenseAccount);
        return expenseAccountRepository.save(expenseAccount);
    }

    /**
     * Get all the expenseAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExpenseAccount> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseAccounts");
        return expenseAccountRepository.findAll(pageable);
    }


    /**
     * Get one expenseAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExpenseAccount> findOne(Long id) {
        log.debug("Request to get ExpenseAccount : {}", id);
        return expenseAccountRepository.findById(id);
    }

    /**
     * Delete the expenseAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpenseAccount : {}", id);
        expenseAccountRepository.deleteById(id);
    }
}
