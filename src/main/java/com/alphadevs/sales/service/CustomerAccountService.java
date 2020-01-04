package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CustomerAccount;
import com.alphadevs.sales.repository.CustomerAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CustomerAccount}.
 */
@Service
@Transactional
public class CustomerAccountService {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountService.class);

    private final CustomerAccountRepository customerAccountRepository;

    public CustomerAccountService(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    /**
     * Save a customerAccount.
     *
     * @param customerAccount the entity to save.
     * @return the persisted entity.
     */
    public CustomerAccount save(CustomerAccount customerAccount) {
        log.debug("Request to save CustomerAccount : {}", customerAccount);
        return customerAccountRepository.save(customerAccount);
    }

    /**
     * Get all the customerAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAccount> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerAccounts");
        return customerAccountRepository.findAll(pageable);
    }


    /**
     * Get one customerAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAccount> findOne(Long id) {
        log.debug("Request to get CustomerAccount : {}", id);
        return customerAccountRepository.findById(id);
    }

    /**
     * Delete the customerAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerAccount : {}", id);
        customerAccountRepository.deleteById(id);
    }
}
