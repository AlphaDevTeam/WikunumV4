package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.DocumentNumberConfig;
import com.alphadevs.sales.repository.DocumentNumberConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DocumentNumberConfig}.
 */
@Service
@Transactional
public class DocumentNumberConfigService {

    private final Logger log = LoggerFactory.getLogger(DocumentNumberConfigService.class);

    private final DocumentNumberConfigRepository documentNumberConfigRepository;

    public DocumentNumberConfigService(DocumentNumberConfigRepository documentNumberConfigRepository) {
        this.documentNumberConfigRepository = documentNumberConfigRepository;
    }

    /**
     * Save a documentNumberConfig.
     *
     * @param documentNumberConfig the entity to save.
     * @return the persisted entity.
     */
    public DocumentNumberConfig save(DocumentNumberConfig documentNumberConfig) {
        log.debug("Request to save DocumentNumberConfig : {}", documentNumberConfig);
        return documentNumberConfigRepository.save(documentNumberConfig);
    }

    /**
     * Get all the documentNumberConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentNumberConfig> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentNumberConfigs");
        return documentNumberConfigRepository.findAll(pageable);
    }


    /**
     * Get one documentNumberConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentNumberConfig> findOne(Long id) {
        log.debug("Request to get DocumentNumberConfig : {}", id);
        return documentNumberConfigRepository.findById(id);
    }

    /**
     * Delete the documentNumberConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentNumberConfig : {}", id);
        documentNumberConfigRepository.deleteById(id);
    }
}
