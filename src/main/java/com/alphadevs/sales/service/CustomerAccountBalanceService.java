package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CustomerAccountBalance;
import com.alphadevs.sales.repository.CustomerAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CustomerAccountBalance}.
 */
@Service
@Transactional
public class CustomerAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(CustomerAccountBalanceService.class);

    private final CustomerAccountBalanceRepository customerAccountBalanceRepository;

    public CustomerAccountBalanceService(CustomerAccountBalanceRepository customerAccountBalanceRepository) {
        this.customerAccountBalanceRepository = customerAccountBalanceRepository;
    }

    /**
     * Save a customerAccountBalance.
     *
     * @param customerAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public CustomerAccountBalance save(CustomerAccountBalance customerAccountBalance) {
        log.debug("Request to save CustomerAccountBalance : {}", customerAccountBalance);
        return customerAccountBalanceRepository.save(customerAccountBalance);
    }

    /**
     * Get all the customerAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerAccountBalances");
        return customerAccountBalanceRepository.findAll(pageable);
    }


    /**
     * Get one customerAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAccountBalance> findOne(Long id) {
        log.debug("Request to get CustomerAccountBalance : {}", id);
        return customerAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the customerAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerAccountBalance : {}", id);
        customerAccountBalanceRepository.deleteById(id);
    }
}
