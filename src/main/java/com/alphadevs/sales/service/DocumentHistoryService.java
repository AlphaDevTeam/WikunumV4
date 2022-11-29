package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.ChangeLog;
import com.alphadevs.sales.domain.DocumentHistory;
import com.alphadevs.sales.domain.DocumentType;
import com.alphadevs.sales.repository.DocumentHistoryRepository;
import com.alphadevs.sales.repository.DocumentTypeRepository;
import com.alphadevs.sales.security.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.alphadevs.sales.security.AuthoritiesConstants.ADMIN;

/**
 * Service Implementation for managing {@link DocumentHistory}.
 */
@Service
@Transactional
public class DocumentHistoryService {

    private final Logger log = LoggerFactory.getLogger(DocumentHistoryService.class);

    private final DocumentHistoryRepository documentHistoryRepository;
    private final UserService userService;
    private final ChangeLogQueryService changeLogQueryService;
    private final ChangeLogService changeLogService;
    private final DocumentHistoryQueryService documentHistoryQueryService;
    private final DocumentTypeRepository documentTypeRepository;

    public DocumentHistoryService(DocumentHistoryRepository documentHistoryRepository, UserService userService, ChangeLogQueryService changeLogQueryService, ChangeLogService changeLogService, DocumentHistoryQueryService documentHistoryQueryService, DocumentTypeRepository documentTypeRepository) {
        this.documentHistoryRepository = documentHistoryRepository;
        this.userService = userService;
        this.changeLogQueryService = changeLogQueryService;
        this.changeLogService = changeLogService;
        this.documentHistoryQueryService = documentHistoryQueryService;
        this.documentTypeRepository = documentTypeRepository;
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

    @Transactional
    public DocumentHistory generateDocumentHistory(String docType , String action , String documentRef,Object object){
        DocumentHistory documentHistory = new DocumentHistory();
        if(SecurityUtils.isCurrentUserInRole(ADMIN) && userService.getExUser().isPresent()){
            DocumentType documentType = getDocumentType(docType);
            documentHistory.setCreatedUser(userService.getExUser().get());
            documentHistory.setHistoryDate(java.time.ZonedDateTime.now());
            documentHistory.setType(documentType);
            documentHistory.setChangeLogs(addChangeLog(documentHistory, " ",object.toString()));
            documentHistory.setHistoryDescription(documentType.getDocumentType() + " " + action + " [" + documentRef +  "] " );
            return save(documentHistory);
        }
        return documentHistory;
    }

    @Transactional
    public DocumentType getDocumentType(String documentTypeCode){
        DocumentType returnDocumentType = null;
        List<DocumentType> byDocumentTypes = documentTypeRepository.findByDocumentTypeCode(documentTypeCode);
        if(byDocumentTypes != null && byDocumentTypes.size() == 1){
            returnDocumentType = byDocumentTypes.get(0);
        }
        Assert.isTrue(returnDocumentType != null, "Document Type Not Found for " + documentTypeCode);
        Assert.isTrue(byDocumentTypes.size() == 1, "Multiple Document Types Found for " + documentTypeCode);
        return returnDocumentType;
    }

    @Transactional
    public Set<ChangeLog> addChangeLog(DocumentHistory documentHistory,String from , String to){
        Set<ChangeLog> changeLogs = new HashSet<>();
        if(documentHistory != null && documentHistory.getChangeLogs() != null &&  !documentHistory.getChangeLogs().isEmpty()){
            changeLogs = documentHistory.getChangeLogs();
        }

        ChangeLog changeLog = new ChangeLog();
        changeLog.addDocumentHistory(documentHistory);
        changeLog.setChangeKey("OBJECT");
        changeLog.setChangeFrom(from);
        changeLog.setChangeTo(to);
        changeLogs.add(changeLog);
        changeLogService.save(changeLog);
        return changeLogs;
    }
}
