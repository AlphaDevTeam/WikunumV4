package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashPaymentVoucherCustomer;
import com.alphadevs.sales.repository.CashPaymentVoucherCustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashPaymentVoucherCustomer}.
 */
@Service
@Transactional
public class CashPaymentVoucherCustomerService {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherCustomerService.class);

    private final CashPaymentVoucherCustomerRepository cashPaymentVoucherCustomerRepository;

    public CashPaymentVoucherCustomerService(CashPaymentVoucherCustomerRepository cashPaymentVoucherCustomerRepository) {
        this.cashPaymentVoucherCustomerRepository = cashPaymentVoucherCustomerRepository;
    }

    /**
     * Save a cashPaymentVoucherCustomer.
     *
     * @param cashPaymentVoucherCustomer the entity to save.
     * @return the persisted entity.
     */
    public CashPaymentVoucherCustomer save(CashPaymentVoucherCustomer cashPaymentVoucherCustomer) {
        log.debug("Request to save CashPaymentVoucherCustomer : {}", cashPaymentVoucherCustomer);
        return cashPaymentVoucherCustomerRepository.save(cashPaymentVoucherCustomer);
    }

    /**
     * Get all the cashPaymentVoucherCustomers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucherCustomer> findAll(Pageable pageable) {
        log.debug("Request to get all CashPaymentVoucherCustomers");
        return cashPaymentVoucherCustomerRepository.findAll(pageable);
    }


    /**
     * Get one cashPaymentVoucherCustomer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashPaymentVoucherCustomer> findOne(Long id) {
        log.debug("Request to get CashPaymentVoucherCustomer : {}", id);
        return cashPaymentVoucherCustomerRepository.findById(id);
    }

    /**
     * Delete the cashPaymentVoucherCustomer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashPaymentVoucherCustomer : {}", id);
        cashPaymentVoucherCustomerRepository.deleteById(id);
    }
}
