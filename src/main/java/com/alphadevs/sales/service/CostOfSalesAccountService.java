package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CostOfSalesAccount;
import com.alphadevs.sales.repository.CostOfSalesAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CostOfSalesAccount}.
 */
@Service
@Transactional
public class CostOfSalesAccountService {

    private final Logger log = LoggerFactory.getLogger(CostOfSalesAccountService.class);

    private final CostOfSalesAccountRepository costOfSalesAccountRepository;

    public CostOfSalesAccountService(CostOfSalesAccountRepository costOfSalesAccountRepository) {
        this.costOfSalesAccountRepository = costOfSalesAccountRepository;
    }

    /**
     * Save a costOfSalesAccount.
     *
     * @param costOfSalesAccount the entity to save.
     * @return the persisted entity.
     */
    public CostOfSalesAccount save(CostOfSalesAccount costOfSalesAccount) {
        log.debug("Request to save CostOfSalesAccount : {}", costOfSalesAccount);
        return costOfSalesAccountRepository.save(costOfSalesAccount);
    }

    /**
     * Get all the costOfSalesAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CostOfSalesAccount> findAll(Pageable pageable) {
        log.debug("Request to get all CostOfSalesAccounts");
        return costOfSalesAccountRepository.findAll(pageable);
    }


    /**
     * Get one costOfSalesAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CostOfSalesAccount> findOne(Long id) {
        log.debug("Request to get CostOfSalesAccount : {}", id);
        return costOfSalesAccountRepository.findById(id);
    }

    /**
     * Delete the costOfSalesAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CostOfSalesAccount : {}", id);
        costOfSalesAccountRepository.deleteById(id);
    }
}
