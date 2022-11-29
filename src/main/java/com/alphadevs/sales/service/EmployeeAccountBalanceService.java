package com.alphadevs.sales.service;

import com.alphadevs.sales.domain.EmployeeAccountBalance;
import com.alphadevs.sales.repository.EmployeeAccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EmployeeAccountBalance}.
 */
@Service
@Transactional
public class EmployeeAccountBalanceService {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountBalanceService.class);

    private final EmployeeAccountBalanceRepository employeeAccountBalanceRepository;

    public EmployeeAccountBalanceService(EmployeeAccountBalanceRepository employeeAccountBalanceRepository) {
        this.employeeAccountBalanceRepository = employeeAccountBalanceRepository;
    }

    /**
     * Save a employeeAccountBalance.
     *
     * @param employeeAccountBalance the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAccountBalance save(EmployeeAccountBalance employeeAccountBalance) {
        log.debug("Request to save EmployeeAccountBalance : {}", employeeAccountBalance);
        return employeeAccountBalanceRepository.save(employeeAccountBalance);
    }

    /**
     * Get all the employeeAccountBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeAccountBalance> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeAccountBalances");
        return employeeAccountBalanceRepository.findAll(pageable);
    }


    /**
     * Get one employeeAccountBalance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeAccountBalance> findOne(Long id) {
        log.debug("Request to get EmployeeAccountBalance : {}", id);
        return employeeAccountBalanceRepository.findById(id);
    }

    /**
     * Delete the employeeAccountBalance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmployeeAccountBalance : {}", id);
        employeeAccountBalanceRepository.deleteById(id);
    }
}
