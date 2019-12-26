package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.PaymentTypeBalance;
import com.alphadevs.sales.repository.PaymentTypeBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentTypeBalance}.
 */
@Service
@Transactional
public class PaymentTypeBalanceService {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeBalanceService.class);

    private final PaymentTypeBalanceRepository paymentTypeBalanceRepository;

    public PaymentTypeBalanceService(PaymentTypeBalanceRepository paymentTypeBalanceRepository) {
        this.paymentTypeBalanceRepository = paymentTypeBalanceRepository;
    }

    /**
     * Save a paymentTypeBalance.
     *
     * @param paymentTypeBalance the entity to save.
     * @return the persisted entity.
     */
    public PaymentTypeBalance save(PaymentTypeBalance paymentTypeBalance) {
        log.debug("Request to save PaymentTypeBalance : {}", paymentTypeBalance);
        return paymentTypeBalanceRepository.save(paymentTypeBalance);
    }

    /**
     * Get all the paymentTypeBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentTypeBalance> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentTypeBalances");
        return paymentTypeBalanceRepository.findAll(pageable);
    }


    /**
     * Get one paymentTypeBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentTypeBalance> findOne(Long id) {
        log.debug("Request to get PaymentTypeBalance : {}", id);
        return paymentTypeBalanceRepository.findById(id);
    }

    /**
     * Delete the paymentTypeBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentTypeBalance : {}", id);
        paymentTypeBalanceRepository.deleteById(id);
    }
}
