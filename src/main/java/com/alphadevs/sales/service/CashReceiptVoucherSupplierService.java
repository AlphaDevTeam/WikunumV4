package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.CashReceiptVoucherSupplier;
import com.alphadevs.sales.repository.CashReceiptVoucherSupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CashReceiptVoucherSupplier}.
 */
@Service
@Transactional
public class CashReceiptVoucherSupplierService {

    private final Logger log = LoggerFactory.getLogger(CashReceiptVoucherSupplierService.class);

    private final CashReceiptVoucherSupplierRepository cashReceiptVoucherSupplierRepository;

    public CashReceiptVoucherSupplierService(CashReceiptVoucherSupplierRepository cashReceiptVoucherSupplierRepository) {
        this.cashReceiptVoucherSupplierRepository = cashReceiptVoucherSupplierRepository;
    }

    /**
     * Save a cashReceiptVoucherSupplier.
     *
     * @param cashReceiptVoucherSupplier the entity to save.
     * @return the persisted entity.
     */
    public CashReceiptVoucherSupplier save(CashReceiptVoucherSupplier cashReceiptVoucherSupplier) {
        log.debug("Request to save CashReceiptVoucherSupplier : {}", cashReceiptVoucherSupplier);
        return cashReceiptVoucherSupplierRepository.save(cashReceiptVoucherSupplier);
    }

    /**
     * Get all the cashReceiptVoucherSuppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CashReceiptVoucherSupplier> findAll(Pageable pageable) {
        log.debug("Request to get all CashReceiptVoucherSuppliers");
        return cashReceiptVoucherSupplierRepository.findAll(pageable);
    }


    /**
     * Get one cashReceiptVoucherSupplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CashReceiptVoucherSupplier> findOne(Long id) {
        log.debug("Request to get CashReceiptVoucherSupplier : {}", id);
        return cashReceiptVoucherSupplierRepository.findById(id);
    }

    /**
     * Delete the cashReceiptVoucherSupplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CashReceiptVoucherSupplier : {}", id);
        cashReceiptVoucherSupplierRepository.deleteById(id);
    }
}
