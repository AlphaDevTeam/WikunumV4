package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.SalesAccount;
import com.alphadevs.sales.repository.SalesAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SalesAccount}.
 */
@Service
@Transactional
public class SalesAccountService {

    private final Logger log = LoggerFactory.getLogger(SalesAccountService.class);

    private final SalesAccountRepository salesAccountRepository;

    public SalesAccountService(SalesAccountRepository salesAccountRepository) {
        this.salesAccountRepository = salesAccountRepository;
    }

    /**
     * Save a salesAccount.
     *
     * @param salesAccount the entity to save.
     * @return the persisted entity.
     */
    public SalesAccount save(SalesAccount salesAccount) {
        log.debug("Request to save SalesAccount : {}", salesAccount);
        return salesAccountRepository.save(salesAccount);
    }

    /**
     * Get all the salesAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SalesAccount> findAll(Pageable pageable) {
        log.debug("Request to get all SalesAccounts");
        return salesAccountRepository.findAll(pageable);
    }


    /**
     * Get one salesAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalesAccount> findOne(Long id) {
        log.debug("Request to get SalesAccount : {}", id);
        return salesAccountRepository.findById(id);
    }

    /**
     * Delete the salesAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SalesAccount : {}", id);
        salesAccountRepository.deleteById(id);
    }
}
