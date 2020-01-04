package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class SalesAccountBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesAccountBalance.class);
        SalesAccountBalance salesAccountBalance1 = new SalesAccountBalance();
        salesAccountBalance1.setId(1L);
        SalesAccountBalance salesAccountBalance2 = new SalesAccountBalance();
        salesAccountBalance2.setId(salesAccountBalance1.getId());
        assertThat(salesAccountBalance1).isEqualTo(salesAccountBalance2);
        salesAccountBalance2.setId(2L);
        assertThat(salesAccountBalance1).isNotEqualTo(salesAccountBalance2);
        salesAccountBalance1.setId(null);
        assertThat(salesAccountBalance1).isNotEqualTo(salesAccountBalance2);
    }
}
