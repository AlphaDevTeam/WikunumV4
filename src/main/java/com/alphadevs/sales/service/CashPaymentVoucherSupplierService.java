package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashPaymentVoucherSupplier;
import com.alphadevs.sales.repository.CashPaymentVoucherSupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashPaymentVoucherSupplier}.
 */
@Service
@Transactional
public class CashPaymentVoucherSupplierService {

    private final Logger log = LoggerFactory.getLogger(CashPaymentVoucherSupplierService.class);

    private final CashPaymentVoucherSupplierRepository cashPaymentVoucherSupplierRepository;

    public CashPaymentVoucherSupplierService(CashPaymentVoucherSupplierRepository cashPaymentVoucherSupplierRepository) {
        this.cashPaymentVoucherSupplierRepository = cashPaymentVoucherSupplierRepository;
    }

    /**
     * Save a cashPaymentVoucherSupplier.
     *
     * @param cashPaymentVoucherSupplier the entity to save.
     * @return the persisted entity.
     */
    public CashPaymentVoucherSupplier save(CashPaymentVoucherSupplier cashPaymentVoucherSupplier) {
        log.debug("Request to save CashPaymentVoucherSupplier : {}", cashPaymentVoucherSupplier);
        return cashPaymentVoucherSupplierRepository.save(cashPaymentVoucherSupplier);
    }

    /**
     * Get all the cashPaymentVoucherSuppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashPaymentVoucherSupplier> findAll(Pageable pageable) {
        log.debug("Request to get all CashPaymentVoucherSuppliers");
        return cashPaymentVoucherSupplierRepository.findAll(pageable);
    }


    /**
     * Get one cashPaymentVoucherSupplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashPaymentVoucherSupplier> findOne(Long id) {
        log.debug("Request to get CashPaymentVoucherSupplier : {}", id);
        return cashPaymentVoucherSupplierRepository.findById(id);
    }

    /**
     * Delete the cashPaymentVoucherSupplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashPaymentVoucherSupplier : {}", id);
        cashPaymentVoucherSupplierRepository.deleteById(id);
    }
}
