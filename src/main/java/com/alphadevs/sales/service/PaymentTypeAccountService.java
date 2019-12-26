package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.PaymentTypeAccount;
import com.alphadevs.sales.repository.PaymentTypeAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentTypeAccount}.
 */
@Service
@Transactional
public class PaymentTypeAccountService {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeAccountService.class);

    private final PaymentTypeAccountRepository paymentTypeAccountRepository;

    public PaymentTypeAccountService(PaymentTypeAccountRepository paymentTypeAccountRepository) {
        this.paymentTypeAccountRepository = paymentTypeAccountRepository;
    }

    /**
     * Save a paymentTypeAccount.
     *
     * @param paymentTypeAccount the entity to save.
     * @return the persisted entity.
     */
    public PaymentTypeAccount save(PaymentTypeAccount paymentTypeAccount) {
        log.debug("Request to save PaymentTypeAccount : {}", paymentTypeAccount);
        return paymentTypeAccountRepository.save(paymentTypeAccount);
    }

    /**
     * Get all the paymentTypeAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentTypeAccount> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentTypeAccounts");
        return paymentTypeAccountRepository.findAll(pageable);
    }


    /**
     * Get one paymentTypeAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentTypeAccount> findOne(Long id) {
        log.debug("Request to get PaymentTypeAccount : {}", id);
        return paymentTypeAccountRepository.findById(id);
    }

    /**
     * Delete the paymentTypeAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentTypeAccount : {}", id);
        paymentTypeAccountRepository.deleteById(id);
    }
}
