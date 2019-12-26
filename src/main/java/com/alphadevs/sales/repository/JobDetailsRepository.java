package com.alphadevs.sales.repository;

import com.alphadevs.sales.domain.JobDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the JobDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, Long>, JpaSpecificationExecutor<JobDetails> {

}
