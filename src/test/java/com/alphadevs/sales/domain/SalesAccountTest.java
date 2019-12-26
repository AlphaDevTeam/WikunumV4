package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class SalesAccountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesAccount.class);
        SalesAccount salesAccount1 = new SalesAccount();
        salesAccount1.setId(1L);
        SalesAccount salesAccount2 = new SalesAccount();
        salesAccount2.setId(salesAccount1.getId());
        assertThat(salesAccount1).isEqualTo(salesAccount2);
        salesAccount2.setId(2L);
        assertThat(salesAccount1).isNotEqualTo(salesAccount2);
        salesAccount1.setId(null);
        assertThat(salesAccount1).isNotEqualTo(salesAccount2);
    }
}
