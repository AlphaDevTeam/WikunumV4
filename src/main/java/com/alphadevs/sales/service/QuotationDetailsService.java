package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.QuotationDetails;
import com.alphadevs.sales.repository.QuotationDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link QuotationDetails}.
 */
@Service
@Transactional
public class QuotationDetailsService {

    private final Logger log = LoggerFactory.getLogger(QuotationDetailsService.class);

    private final QuotationDetailsRepository quotationDetailsRepository;

    public QuotationDetailsService(QuotationDetailsRepository quotationDetailsRepository) {
        this.quotationDetailsRepository = quotationDetailsRepository;
    }

    /**
     * Save a quotationDetails.
     *
     * @param quotationDetails the entity to save.
     * @return the persisted entity.
     */
    public QuotationDetails save(QuotationDetails quotationDetails) {
        log.debug("Request to save QuotationDetails : {}", quotationDetails);
        return quotationDetailsRepository.save(quotationDetails);
    }

    /**
     * Get all the quotationDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationDetails> findAll(Pageable pageable) {
        log.debug("Request to get all QuotationDetails");
        return quotationDetailsRepository.findAll(pageable);
    }


    /**
     * Get one quotationDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuotationDetails> findOne(Long id) {
        log.debug("Request to get QuotationDetails : {}", id);
        return quotationDetailsRepository.findById(id);
    }

    /**
     * Delete the quotationDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuotationDetails : {}", id);
        quotationDetailsRepository.deleteById(id);
    }
}
