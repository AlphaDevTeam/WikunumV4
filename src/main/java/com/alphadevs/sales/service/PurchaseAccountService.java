package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.PurchaseAccount;
import com.alphadevs.sales.repository.PurchaseAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PurchaseAccount}.
 */
@Service
@Transactional
public class PurchaseAccountService {

    private final Logger log = LoggerFactory.getLogger(PurchaseAccountService.class);

    private final PurchaseAccountRepository purchaseAccountRepository;

    public PurchaseAccountService(PurchaseAccountRepository purchaseAccountRepository) {
        this.purchaseAccountRepository = purchaseAccountRepository;
    }

    /**
     * Save a purchaseAccount.
     *
     * @param purchaseAccount the entity to save.
     * @return the persisted entity.
     */
    public PurchaseAccount save(PurchaseAccount purchaseAccount) {
        log.debug("Request to save PurchaseAccount : {}", purchaseAccount);
        return purchaseAccountRepository.save(purchaseAccount);
    }

    /**
     * Get all the purchaseAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseAccount> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseAccounts");
        return purchaseAccountRepository.findAll(pageable);
    }


    /**
     * Get one purchaseAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseAccount> findOne(Long id) {
        log.debug("Request to get PurchaseAccount : {}", id);
        return purchaseAccountRepository.findById(id);
    }

    /**
     * Delete the purchaseAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseAccount : {}", id);
        purchaseAccountRepository.deleteById(id);
    }
}
