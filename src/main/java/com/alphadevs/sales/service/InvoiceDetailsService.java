package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.InvoiceDetails;
import com.alphadevs.sales.repository.InvoiceDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InvoiceDetails}.
 */
@Service
@Transactional
public class InvoiceDetailsService {

    private final Logger log = LoggerFactory.getLogger(InvoiceDetailsService.class);

    private final InvoiceDetailsRepository invoiceDetailsRepository;

    public InvoiceDetailsService(InvoiceDetailsRepository invoiceDetailsRepository) {
        this.invoiceDetailsRepository = invoiceDetailsRepository;
    }

    /**
     * Save a invoiceDetails.
     *
     * @param invoiceDetails the entity to save.
     * @return the persisted entity.
     */
    public InvoiceDetails save(InvoiceDetails invoiceDetails) {
        log.debug("Request to save InvoiceDetails : {}", invoiceDetails);
        return invoiceDetailsRepository.save(invoiceDetails);
    }

    /**
     * Get all the invoiceDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDetails> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceDetails");
        return invoiceDetailsRepository.findAll(pageable);
    }


    /**
     * Get one invoiceDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceDetails> findOne(Long id) {
        log.debug("Request to get InvoiceDetails : {}", id);
        return invoiceDetailsRepository.findById(id);
    }

    /**
     * Delete the invoiceDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceDetails : {}", id);
        invoiceDetailsRepository.deleteById(id);
    }
}
