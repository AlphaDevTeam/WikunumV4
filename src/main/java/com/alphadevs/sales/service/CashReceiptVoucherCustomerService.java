package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashReceiptVoucherCustomer;
import com.alphadevs.sales.repository.CashReceiptVoucherCustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashReceiptVoucherCustomer}.
 */
@Service
@Transactional
public class CashReceiptVoucherCustomerService {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherCustomerService.class);

    private final CashReceiptVoucherCustomerRepository cashReceiptVoucherCustomerRepository;

    public CashReceiptVoucherCustomerService(CashReceiptVoucherCustomerRepository cashReceiptVoucherCustomerRepository) {
        this.cashReceiptVoucherCustomerRepository = cashReceiptVoucherCustomerRepository;
    }

    /**
     * Save a cashReceiptVoucherCustomer.
     *
     * @param cashReceiptVoucherCustomer the entity to save.
     * @return the persisted entity.
     */
    public CashReceiptVoucherCustomer save(CashReceiptVoucherCustomer cashReceiptVoucherCustomer) {
        log.debug("Request to save CashReceiptVoucherCustomer : {}", cashReceiptVoucherCustomer);
        return cashReceiptVoucherCustomerRepository.save(cashReceiptVoucherCustomer);
    }

    /**
     * Get all the cashReceiptVoucherCustomers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashReceiptVoucherCustomer> findAll(Pageable pageable) {
        log.debug("Request to get all CashReceiptVoucherCustomers");
        return cashReceiptVoucherCustomerRepository.findAll(pageable);
    }


    /**
     * Get one cashReceiptVoucherCustomer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashReceiptVoucherCustomer> findOne(Long id) {
        log.debug("Request to get CashReceiptVoucherCustomer : {}", id);
        return cashReceiptVoucherCustomerRepository.findById(id);
    }

    /**
     * Delete the cashReceiptVoucherCustomer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashReceiptVoucherCustomer : {}", id);
        cashReceiptVoucherCustomerRepository.deleteById(id);
    }
}
