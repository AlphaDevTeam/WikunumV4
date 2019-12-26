package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.ChangeLog;
import com.alphadevs.sales.repository.ChangeLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChangeLog}.
 */
@Service
@Transactional
public class ChangeLogService {

    private final Logger log = LoggerFactory.getLogger(ChangeLogService.class);

    private final ChangeLogRepository changeLogRepository;

    public ChangeLogService(ChangeLogRepository changeLogRepository) {
        this.changeLogRepository = changeLogRepository;
    }

    /**
     * Save a changeLog.
     *
     * @param changeLog the entity to save.
     * @return the persisted entity.
     */
    public ChangeLog save(ChangeLog changeLog) {
        log.debug("Request to save ChangeLog : {}", changeLog);
        return changeLogRepository.save(changeLog);
    }

    /**
     * Get all the changeLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChangeLog> findAll(Pageable pageable) {
        log.debug("Request to get all ChangeLogs");
        return changeLogRepository.findAll(pageable);
    }


    /**
     * Get one changeLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChangeLog> findOne(Long id) {
        log.debug("Request to get ChangeLog : {}", id);
        return changeLogRepository.findById(id);
    }

    /**
     * Delete the changeLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChangeLog : {}", id);
        changeLogRepository.deleteById(id);
    }
}
