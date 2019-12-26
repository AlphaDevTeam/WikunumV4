package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class ChangeLogTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChangeLog.class);
        ChangeLog changeLog1 = new ChangeLog();
        changeLog1.setId(1L);
        ChangeLog changeLog2 = new ChangeLog();
        changeLog2.setId(changeLog1.getId());
        assertThat(changeLog1).isEqualTo(changeLog2);
        changeLog2.setId(2L);
        assertThat(changeLog1).isNotEqualTo(changeLog2);
        changeLog1.setId(null);
        assertThat(changeLog1).isNotEqualTo(changeLog2);
    }
}
