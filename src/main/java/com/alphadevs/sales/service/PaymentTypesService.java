package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.PaymentTypes;
import com.alphadevs.sales.repository.PaymentTypesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentTypes}.
 */
@Service
@Transactional
public class PaymentTypesService {

    private final Logger log = LoggerFactory.getLogger(PaymentTypesService.class);

    private final PaymentTypesRepository paymentTypesRepository;

    public PaymentTypesService(PaymentTypesRepository paymentTypesRepository) {
        this.paymentTypesRepository = paymentTypesRepository;
    }

    /**
     * Save a paymentTypes.
     *
     * @param paymentTypes the entity to save.
     * @return the persisted entity.
     */
    public PaymentTypes save(PaymentTypes paymentTypes) {
        log.debug("Request to save PaymentTypes : {}", paymentTypes);
        return paymentTypesRepository.save(paymentTypes);
    }

    /**
     * Get all the paymentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentTypes> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentTypes");
        return paymentTypesRepository.findAll(pageable);
    }


    /**
     * Get one paymentTypes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentTypes> findOne(Long id) {
        log.debug("Request to get PaymentTypes : {}", id);
        return paymentTypesRepository.findById(id);
    }

    /**
     * Delete the paymentTypes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentTypes : {}", id);
        paymentTypesRepository.deleteById(id);
    }
}
