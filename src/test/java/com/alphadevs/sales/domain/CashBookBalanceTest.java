package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashBookBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashBookBalance.class);
        CashBookBalance cashBookBalance1 = new CashBookBalance();
        cashBookBalance1.setId(1L);
        CashBookBalance cashBookBalance2 = new CashBookBalance();
        cashBookBalance2.setId(cashBookBalance1.getId());
        assertThat(cashBookBalance1).isEqualTo(cashBookBalance2);
        cashBookBalance2.setId(2L);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
        cashBookBalance1.setId(null);
        assertThat(cashBookBalance1).isNotEqualTo(cashBookBalance2);
    }
}
