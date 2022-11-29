package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.Company;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.repository.CompanyRepository;
import com.alphadevs.sales.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.alphadevs.sales.security.AuthoritiesConstants.ADMIN;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);
    private final String DOC_TYPE_CODE = "COMPANY";

    private final CompanyRepository companyRepository;
    private final DocumentHistoryService documentHistoryService;

    public CompanyService(CompanyRepository companyRepository, DocumentHistoryService documentHistoryService) {
        this.companyRepository = companyRepository;
        this.documentHistoryService = documentHistoryService;
    }

    /**
     * Save a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        DocumentHistory documentHistory = documentHistoryService.generateDocumentHistory(DOC_TYPE_CODE, "Save Request", company.getCompanyCode(),company);
        company.setHistory(documentHistory);
        return companyRepository.save(company);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable);
    }


    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
