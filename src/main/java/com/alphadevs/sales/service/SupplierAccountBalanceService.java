package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.SupplierAccountBalance;
import com.alphadevs.sales.repository.SupplierAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SupplierAccountBalance}.
 */
@Service
@Transactional
public class SupplierAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(SupplierAccountBalanceService.class);

    private final SupplierAccountBalanceRepository supplierAccountBalanceRepository;

    public SupplierAccountBalanceService(SupplierAccountBalanceRepository supplierAccountBalanceRepository) {
        this.supplierAccountBalanceRepository = supplierAccountBalanceRepository;
    }

    /**
     * Save a supplierAccountBalance.
     *
     * @param supplierAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public SupplierAccountBalance save(SupplierAccountBalance supplierAccountBalance) {
        log.debug("Request to save SupplierAccountBalance : {}", supplierAccountBalance);
        return supplierAccountBalanceRepository.save(supplierAccountBalance);
    }

    /**
     * Get all the supplierAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierAccountBalances");
        return supplierAccountBalanceRepository.findAll(pageable);
    }


    /**
     * Get one supplierAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAccountBalance> findOne(Long id) {
        log.debug("Request to get SupplierAccountBalance : {}", id);
        return supplierAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the supplierAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SupplierAccountBalance : {}", id);
        supplierAccountBalanceRepository.deleteById(id);
    }
}
