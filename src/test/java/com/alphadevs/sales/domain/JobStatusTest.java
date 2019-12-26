package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class JobStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobStatus.class);
        JobStatus jobStatus1 = new JobStatus();
        jobStatus1.setId(1L);
        JobStatus jobStatus2 = new JobStatus();
        jobStatus2.setId(jobStatus1.getId());
        assertThat(jobStatus1).isEqualTo(jobStatus2);
        jobStatus2.setId(2L);
        assertThat(jobStatus1).isNotEqualTo(jobStatus2);
        jobStatus1.setId(null);
        assertThat(jobStatus1).isNotEqualTo(jobStatus2);
    }
}
