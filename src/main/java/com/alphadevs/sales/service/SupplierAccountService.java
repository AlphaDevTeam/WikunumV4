package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.SupplierAccount;
import com.alphadevs.sales.repository.SupplierAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SupplierAccount}.
 */
@Service
@Transactional
public class SupplierAccountService {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountService.class);

    private final SupplierAccountRepository supplierAccountRepository;

    public SupplierAccountService(SupplierAccountRepository supplierAccountRepository) {
        this.supplierAccountRepository = supplierAccountRepository;
    }

    /**
     * Save a supplierAccount.
     *
     * @param supplierAccount the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccount save(SupplierAccount supplierAccount) {
        log.debug("Request to save SupplierAccount : {}", supplierAccount);
        return supplierAccountRepository.save(supplierAccount);
    }

    /**
     * Get all the supplierAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccount> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierAccounts");
        return supplierAccountRepository.findAll(pageable);
    }


    /**
     * Get one supplierAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAccount> findOne(Long id) {
        log.debug("Request to get SupplierAccount : {}", id);
        return supplierAccountRepository.findById(id);
    }

    /**
     * Delete the supplierAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SupplierAccount : {}", id);
        supplierAccountRepository.deleteById(id);
    }
}
