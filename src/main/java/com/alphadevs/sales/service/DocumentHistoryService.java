package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.repository.DocumentHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DocumentHistory}.
 */
@Service
@Transactional
public class DocumentHistoryService {

    private final Logger log = LoggerFactory.getLogger(DocumentHistoryService.class);

    private final DocumentHistoryRepository documentHistoryRepository;

    public DocumentHistoryService(DocumentHistoryRepository documentHistoryRepository) {
        this.documentHistoryRepository = documentHistoryRepository;
    }

    /**
     * Save a documentHistory.
     *
     * @param documentHistory the entity to save.
     * @return the persisted entity.
     */
    public DocumentHistory save(DocumentHistory documentHistory) {
        log.debug("Request to save DocumentHistory : {}", documentHistory);
        return documentHistoryRepository.save(documentHistory);
    }

    /**
     * Get all the documentHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentHistory> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentHistories");
        return documentHistoryRepository.findAll(pageable);
    }

    /**
     * Get all the documentHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DocumentHistory> findAllWithEagerRelationships(Pageable pageable) {
        return documentHistoryRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one documentHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentHistory> findOne(Long id) {
        log.debug("Request to get DocumentHistory : {}", id);
        return documentHistoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the documentHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentHistory : {}", id);
        documentHistoryRepository.deleteById(id);
    }
}
